package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import com.alienlab.ziranli.web.wechat.controller.Wechat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CourseOrder.
 */
public interface CourseOrderService {

    /**
     * Save a courseOrder.
     *
     * @param courseOrder the entity to save
     * @return the persisted entity
     */
    CourseOrder save(CourseOrder courseOrder);

    /**
     *  Get all the courseOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseOrder> findAll(Pageable pageable);

    /**
     *  Get the "id" courseOrder.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseOrder findOne(Long id);

    /**
     *  Delete the "id" courseOrder.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<CourseOrder> findCourseByUser(WechatUser user, String status)throws Exception;
    List<CourseOrder> findOrderByCourseUser(WechatUser user, Course course, String status)throws Exception;
    //查询个人课程购买记录
    List<CourseOrder> findMyCourseOrder(WechatUser wechatUser);
}
