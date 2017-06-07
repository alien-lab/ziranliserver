package com.alienlab.ziranli.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.service.ArtworkService;
import com.alienlab.ziranli.web.rest.util.ExecResult;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import com.alienlab.ziranli.web.wechat.service.WechatMessageService;
import com.alienlab.ziranli.web.wechat.service.WechatService;
import com.alienlab.ziranli.web.wechat.service.WechatUserService;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.service.ArtworkOrderService;
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
import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing ArtworkOrder.
 */
@RestController
@RequestMapping("/api")
public class ArtworkOrderResource {

    private final Logger log = LoggerFactory.getLogger(ArtworkOrderResource.class);

    private static final String ENTITY_NAME = "artworkOrder";

    @Autowired
    ArtworkService artworkService;

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    WechatService wechatService;

    @Autowired
    WechatMessageService wechatMessageService;

    private final ArtworkOrderService artworkOrderService;

    public ArtworkOrderResource(ArtworkOrderService artworkOrderService) {
        this.artworkOrderService = artworkOrderService;
    }

    /**
     * POST  /artwork-orders : Create a new artworkOrder.
     *
     * @param artworkOrder the artworkOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new artworkOrder, or with status 400 (Bad Request) if the artworkOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/artwork-orders")
    @Timed
    public ResponseEntity<ArtworkOrder> createArtworkOrder(@RequestBody ArtworkOrder artworkOrder) throws URISyntaxException {
        log.debug("REST request to save ArtworkOrder : {}", artworkOrder);
        if (artworkOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new artworkOrder cannot already have an ID")).body(null);
        }
        ArtworkOrder result = artworkOrderService.save(artworkOrder);
        return ResponseEntity.created(new URI("/api/artwork-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



//    @ApiOperation("获取当前用户下单情况")
//    @GetMapping("/artwork-orders/{openid}/{artId}")
//    public ResponseEntity getOrder(@PathVariable String openid,@PathVariable long artId){
//        return null;
//    }



    @ApiOperation("艺术品下单")
    @PostMapping("/artwork-orders/json")
    @Timed
    public ResponseEntity createArtworkOrder(@RequestBody String joinJson,HttpServletRequest request) throws URISyntaxException {
        log.debug("createArtworkOrder>>>>"+joinJson);
        JSONObject order=JSONObject.parseObject(joinJson);
        String openid=order.getString("openid");
        WechatUser user=wechatUserService.findUserByOpenid(openid);
        if(user==null){
            ExecResult er=new ExecResult(false,"用户id不存在,"+openid);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        Long artId=order.getLong("artId");
        Artwork artwork=artworkService.findOne(artId);
        ArtworkOrder artworkOrder=null;
        if(artwork==null){
            ExecResult er=new ExecResult(false,"未找到ID为"+artId+"的艺术品");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if(artwork.getAmount()<1){
            ExecResult er=new ExecResult(false,"艺术品已售罄");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        try {
            List<ArtworkOrder> existsOrders=artworkOrderService.findUnpayOrder(user,artwork);
            //存在已有订单，构造已有订单返回
            if(existsOrders!=null&&existsOrders.size()>0){
                artworkOrder=existsOrders.get(0);
            }

        } catch (Exception e) {
            ExecResult er=new ExecResult(false,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }

        if(artworkOrder==null){
            artworkOrder=new ArtworkOrder();
        }
        artworkOrder.setAmount(1);
        artworkOrder.setAddress(order.getString("address"));
        artworkOrder.setContact(order.getString("contact"));
        artworkOrder.setArtwork(artwork);
        artworkOrder.setOrderTime(ZonedDateTime.now());
        Float price=artwork.getPrice();
        artworkOrder.setPayPrice(price);
        artworkOrder.setPayStatus("未支付");
        artworkOrder.setPhone(order.getString("phone"));
        String orderno=UUID.randomUUID().toString().replaceAll("-","");
        artworkOrder.setWechatOrderno(orderno);
        artworkOrder.setUser(user);

        user.setAddress(order.getString("address"));
        user.setPhone(order.getString("phone"));
        user.setName(order.getString("contact"));

        wechatUserService.save(user);

        //微信下单支付
        Map<String,String> orderResult=wechatService.makeOrder(artwork.getName(),artworkOrder.getWechatOrderno(),
            artworkOrder.getPayPrice().intValue(),request.getRemoteAddr(),openid);

        if(orderResult==null){
            ExecResult er=new ExecResult(false,"调用微信支付失败。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        String orderflag=orderResult.get("return_code");
        String resultcode=orderResult.get("result_code");
        if(orderflag.equalsIgnoreCase("SUCCESS")) {
            if (resultcode.equalsIgnoreCase("SUCCESS")) {//订单创建成功
                JSONObject orderInfo=wechatService.getPayParam(orderResult);
                artworkOrder=artworkOrderService.save(artworkOrder);
                JSONObject result=new JSONObject();
                result.put("artworkOrder",artworkOrder);
                result.put("orderInfo",orderInfo);
                return ResponseEntity.ok().body(result);
            }else{ //如果下单出现错误，返回错误信息到页面
                ExecResult er=new ExecResult(false,orderResult.get("err_code_des"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{ //如果获取订单错误，返回错误信息到页面
            ExecResult er=new ExecResult(false,orderResult.get("return_msg"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation("支付艺术品订单")
    @PostMapping("/artwork-orders/pay")
    @Timed
    public ResponseEntity payOrder(@RequestBody Map param){
        log.debug("payOrder>>>"+JSON.toJSONString(param));
        Long orderId=TypeUtils.castToLong(param.get("orderId"));
        String openid=TypeUtils.castToString(param.get("openid"));
        ArtworkOrder artworkOrder=artworkOrderService.findOne(orderId);
        if(artworkOrder==null){
            ExecResult er=new ExecResult(false,"查询不到订单信息,"+orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        Map<String,String> orderinfo=wechatService.getOrder(artworkOrder.getWechatOrderno());
        log.info("orderpay>>>"+ JSON.toJSONString(orderinfo));
        if(orderinfo==null){
            ExecResult er=new ExecResult(false,"获取订单信息出错");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        String returncode=orderinfo.get("return_code");
        String resultcode=orderinfo.get("result_code");
        if(!artworkOrder.getUser().getOpenId().equals(openid)){
            ExecResult er=new ExecResult(false,"订单创建用户与支付用户不匹配，支付失败。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if(orderinfo.containsKey("trade_state")){
            String tradestatus=orderinfo.get("trade_state");
            if(tradestatus.equalsIgnoreCase("SUCCESS")){
                artworkOrder.setPayTime(ZonedDateTime.now());
                artworkOrder.setPayStatus("已支付");
                //更新艺术品存量
                Artwork artwork=artworkOrder.getArtwork();
                artwork.setAmount(artwork.getAmount()-1);
                if(artwork.getAmount()==0){
                    artwork.setStatus("售罄");
                }
                artworkService.save(artwork);
                artworkOrder=artworkOrderService.save(artworkOrder);
                //发送微信消息推送
                wechatMessageService.buyArtworkMessage(artworkOrder);

                return ResponseEntity.ok().body(artworkOrder);
            }else{
                ExecResult er=new ExecResult(false,"微信支付未成功，支付状态："+tradestatus);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{
            ExecResult er=new ExecResult(false,"微信订单数据获取失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    /**
     * PUT  /artwork-orders : Updates an existing artworkOrder.
     *
     * @param artworkOrder the artworkOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artworkOrder,
     * or with status 400 (Bad Request) if the artworkOrder is not valid,
     * or with status 500 (Internal Server Error) if the artworkOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/artwork-orders")
    @Timed
    public ResponseEntity<ArtworkOrder> updateArtworkOrder(@RequestBody ArtworkOrder artworkOrder) throws URISyntaxException {
        log.debug("REST request to update ArtworkOrder : {}", artworkOrder);
        if (artworkOrder.getId() == null) {
            return createArtworkOrder(artworkOrder);
        }
        ArtworkOrder result = artworkOrderService.save(artworkOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, artworkOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artwork-orders : get all the artworkOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of artworkOrders in body
     */
    @GetMapping("/artwork-orders")
    @Timed
    public ResponseEntity<List<ArtworkOrder>> getAllArtworkOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ArtworkOrders");
        Page<ArtworkOrder> page = artworkOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artwork-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artwork-orders/:id : get the "id" artworkOrder.
     *
     * @param id the id of the artworkOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artworkOrder, or with status 404 (Not Found)
     */
    @GetMapping("/artwork-orders/{id}")
    @Timed
    public ResponseEntity<ArtworkOrder> getArtworkOrder(@PathVariable Long id) {
        log.debug("REST request to get ArtworkOrder : {}", id);
        ArtworkOrder artworkOrder = artworkOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(artworkOrder));
    }

    /**
     * DELETE  /artwork-orders/:id : delete the "id" artworkOrder.
     *
     * @param id the id of the artworkOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/artwork-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteArtworkOrder(@PathVariable Long id) {
        log.debug("REST request to delete ArtworkOrder : {}", id);
        artworkOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
