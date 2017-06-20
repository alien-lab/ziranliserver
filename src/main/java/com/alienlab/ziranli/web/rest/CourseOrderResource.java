package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.domain.Course;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.ziranli.domain.*;
import com.alienlab.ziranli.service.CourseService;
import com.alienlab.ziranli.service.ShareLogService;
import com.alienlab.ziranli.web.rest.util.ExecResult;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import com.alienlab.ziranli.web.wechat.service.WechatMessageService;
import com.alienlab.ziranli.web.wechat.service.WechatService;
import com.alienlab.ziranli.web.wechat.service.WechatUserService;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.service.CourseOrderService;
import com.alienlab.ziranli.web.rest.util.HeaderUtil;
import com.alienlab.ziranli.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing CourseOrder.
 */
@RestController
@RequestMapping("/api")
public class CourseOrderResource {

    private final Logger log = LoggerFactory.getLogger(CourseOrderResource.class);

    private static final String ENTITY_NAME = "courseOrder";

    private final CourseOrderService courseOrderService;

    @Autowired
    WechatService wechatService;
    @Autowired
    WechatUserService wechatUserService;
    @Autowired
    CourseService courseService;

    @Autowired
    WechatMessageService wechatMessageService;

    @Autowired
    ShareLogService shareLogService;

    public CourseOrderResource(CourseOrderService courseOrderService, WechatUserService wechatUserService) {
        this.courseOrderService = courseOrderService;
        this.wechatUserService = wechatUserService;
    }

    /**
     * POST  /course-orders : Create a new courseOrder.
     *
     * @param courseOrder the courseOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseOrder, or with status 400 (Bad Request) if the courseOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-orders")
    @Timed
    public ResponseEntity<CourseOrder> createCourseOrder(@RequestBody CourseOrder courseOrder) throws URISyntaxException {
        log.debug("REST request to save CourseOrder : {}", courseOrder);
        if (courseOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseOrder cannot already have an ID")).body(null);
        }
        CourseOrder result = courseOrderService.save(courseOrder);
        return ResponseEntity.created(new URI("/api/course-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @ApiOperation("课程购买")
    @PostMapping("/course-orders/json")
    @Timed
    public ResponseEntity addCourseOrder(@RequestBody String json, HttpServletRequest request) {
        JSONObject order = JSONObject.parseObject(json);
        String openid = order.getString("openid");
        WechatUser user = wechatUserService.findUserByOpenid(openid);
        if (user == null) {
            ExecResult er = new ExecResult(false, "用户id不存在," + openid);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        Long courseId = order.getLong("courseId");
        Course course = courseService.findOne(courseId);
        if (course == null) {
            ExecResult er = new ExecResult(false, "未找到ID为" + courseId + "的课程");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        CourseOrder courseOrder = null;
        //读取该用户名下未支付订单
        try {
            List<CourseOrder> existsOrders = courseOrderService.findCourseByUser(user, "未支付");
            if (existsOrders != null && existsOrders.size() > 0) {
                courseOrder = existsOrders.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (courseOrder == null) {
            courseOrder = new CourseOrder();
        }
        List<ShareLog> logs = null;
        try {
            logs = shareLogService.getCourseShareLog(courseId, user.getOpenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        float price = course.getPrice();
        //有用户分享记录，
        if (logs != null && logs.size() > 0) {
            log.debug("find " + logs.size() + " share logs.");
            price = course.getPrice2();
            log.debug("shared price is " + price);
            courseOrder.setIsShare("1");
        } else {
            log.debug("no share log.");
        }

        courseOrder.setCourse(course);
        courseOrder.setOrderTime(ZonedDateTime.now());
        courseOrder.setPayPrice(price);
        courseOrder.setUser(user);

        if (price > 0) {//分享后价格不为免费的情况
            courseOrder.setPayStatus("未支付");
            String orderno = UUID.randomUUID().toString().replaceAll("-", "");
            courseOrder.setWechatOrderno(orderno);
            courseOrder = courseOrderService.save(courseOrder);
            //微信下单
            Map<String, String> orderResult = wechatService.makeOrder(course.getName(), courseOrder.getWechatOrderno(),
                courseOrder.getPayPrice().intValue(), request.getRemoteAddr(), openid);

            if (orderResult == null) {
                ExecResult er = new ExecResult(false, "调用微信支付失败。");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
            String orderflag = orderResult.get("return_code");
            String resultcode = orderResult.get("result_code");
            if (orderflag.equalsIgnoreCase("SUCCESS")) {
                if (resultcode.equalsIgnoreCase("SUCCESS")) {//订单创建成功
                    JSONObject orderInfo = wechatService.getPayParam(orderResult);
                    JSONObject result = new JSONObject();
                    result.put("courseOrder", courseOrder);
                    result.put("orderInfo", orderInfo);
//                    Map onlive=courseService.getCourseOnlive(courseOrder.getCourse().getId());
//                    if(onlive!=null){
//                        result.put("onlive",onlive);
//                    }
                    return ResponseEntity.ok().body(result);
                } else { //如果下单出现错误，返回错误信息到页面
                    ExecResult er = new ExecResult(false, orderResult.get("err_code_des"));
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
                }
            } else { //如果获取订单错误，返回错误信息到页面
                ExecResult er = new ExecResult(false, orderResult.get("return_msg"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        } else {//免费的情况
            log.debug("shared price is free.return a finish order");
            courseOrder.setWechatOrderno("noorder");
            courseOrder.setPayTime(ZonedDateTime.now());
            courseOrder.setPayStatus("已支付");
            courseOrder = courseOrderService.save(courseOrder);
            JSONObject result = new JSONObject();
            result.put("courseOrder", courseOrder);
            Map onlive = courseService.getCourseOnlive(courseOrder.getCourse().getId());
            if (onlive != null) {
                result.put("onlive", onlive);
            }
            return ResponseEntity.ok().body(result);
        }
    }

    @ApiOperation("支付艺术品订单")
    @PostMapping("/course-orders/pay")
    @Timed
    public ResponseEntity payOrder(@RequestBody Map param) {
        log.debug("payOrder>>>" + JSON.toJSONString(param));
        Long orderId = TypeUtils.castToLong(param.get("orderId"));
        String openid = TypeUtils.castToString(param.get("openid"));
        CourseOrder courseOrder = courseOrderService.findOne(orderId);
        if (courseOrder == null) {
            ExecResult er = new ExecResult(false, "查询不到订单信息," + orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if (courseOrder.getWechatOrderno().equals("noorder") || courseOrder.getPayStatus().equals("已支付")) {
            log.debug("shared order ,no need pay for.");
            ExecResult er = new ExecResult(false, "无需支付的课程订单。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        Map<String, String> orderinfo = wechatService.getOrder(courseOrder.getWechatOrderno());
        log.info("orderpay>>>" + JSON.toJSONString(orderinfo));
        if (orderinfo == null) {
            ExecResult er = new ExecResult(false, "获取微信订单支付信息出错");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        String returncode = orderinfo.get("return_code");
        String resultcode = orderinfo.get("result_code");
        if (!courseOrder.getUser().getOpenId().equals(openid)) {
            ExecResult er = new ExecResult(false, "订单创建用户与支付用户不匹配，支付失败。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if (orderinfo.containsKey("trade_state")) {
            String tradestatus = orderinfo.get("trade_state");
            if (tradestatus.equalsIgnoreCase("SUCCESS")) {
                courseOrder.setPayTime(ZonedDateTime.now());
                courseOrder.setPayStatus("已支付");

                courseOrderService.save(courseOrder);
                //发送微信消息推送
                wechatMessageService.buyCourseMessage(courseOrder);

                JSONObject j = new JSONObject();
                j.put("courseOrder", courseOrder);
                Map onlive = courseService.getCourseOnlive(courseOrder.getCourse().getId());
                if (onlive != null) {
                    j.put("onlive", onlive);
                }
                return ResponseEntity.ok().body(j);
            } else {
                ExecResult er = new ExecResult(false, "微信支付未成功，支付状态：" + tradestatus);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        } else {
            ExecResult er = new ExecResult(false, "微信订单数据获取失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }


    @ApiOperation("获取用户课程购买信息")
    @GetMapping("/course-orders/{courseId}/{openid}")
    @Timed
    public ResponseEntity loadCourseOrderByUser(@PathVariable Long courseId, @PathVariable String openid) {
        WechatUser user = wechatUserService.findUserByOpenid(openid);
        if (user == null) {
            ExecResult er = new ExecResult(false, "微信用户信息获取错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        Course course = courseService.findOne(courseId);

        try {
            List<CourseOrder> result = courseOrderService.findOrderByCourseUser(user, course, "已支付");
            if (result != null && result.size() > 0) {
                Map onlive = courseService.getCourseOnlive(courseId);
                JSONObject j = new JSONObject();
                j.put("courseOrder", (CourseOrder) result.get(0));
                if (onlive != null) {
                    j.put("onlive", onlive);
                }

                return ResponseEntity.ok().body(j);
            } else {
                return ResponseEntity.ok().body(new JSONObject());
            }

        } catch (Exception e) {
            e.printStackTrace();
            ExecResult er = new ExecResult(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    /**
     * PUT  /course-orders : Updates an existing courseOrder.
     *
     * @param courseOrder the courseOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseOrder,
     * or with status 400 (Bad Request) if the courseOrder is not valid,
     * or with status 500 (Internal Server Error) if the courseOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-orders")
    @Timed
    public ResponseEntity<CourseOrder> updateCourseOrder(@RequestBody CourseOrder courseOrder) throws URISyntaxException {
        log.debug("REST request to update CourseOrder : {}", courseOrder);
        if (courseOrder.getId() == null) {
            return createCourseOrder(courseOrder);
        }
        CourseOrder result = courseOrderService.save(courseOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-orders : get all the courseOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseOrders in body
     */
    @GetMapping("/course-orders")
    @Timed
    public ResponseEntity<List<CourseOrder>> getAllCourseOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseOrders");
        Page<CourseOrder> page = courseOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-orders/:id : get the "id" courseOrder.
     *
     * @param id the id of the courseOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseOrder, or with status 404 (Not Found)
     */
    @GetMapping("/course-orders/{id}")
    @Timed
    public ResponseEntity<CourseOrder> getCourseOrder(@PathVariable Long id) {
        log.debug("REST request to get CourseOrder : {}", id);
        CourseOrder courseOrder = courseOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseOrder));
    }

    /**
     * DELETE  /course-orders/:id : delete the "id" courseOrder.
     *
     * @param id the id of the courseOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseOrder(@PathVariable Long id) {
        log.debug("REST request to delete CourseOrder : {}", id);
        courseOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //获取个人课程订单记录
    @GetMapping("/courseOrders/{openid}")
    @Timed
    public ResponseEntity<List<CourseOrder>> getMyCourseOrders(@PathVariable String openid) {
        log.debug("获取个人课程订单");
        WechatUser wechatUser = wechatUserService.findUserByOpenid(openid);
        List<CourseOrder> courseOrders = courseOrderService.findMyCourseOrder(wechatUser);
        return ResponseEntity.ok().body(courseOrders);
    }

}
