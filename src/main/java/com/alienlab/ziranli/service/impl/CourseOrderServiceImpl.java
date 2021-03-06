package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.service.CourseOrderService;
import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.repository.CourseOrderRepository;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing CourseOrder.
 */
@Service
@Transactional
public class CourseOrderServiceImpl implements CourseOrderService{

    private final Logger log = LoggerFactory.getLogger(CourseOrderServiceImpl.class);

    private final CourseOrderRepository courseOrderRepository;

    public CourseOrderServiceImpl(CourseOrderRepository courseOrderRepository) {
        this.courseOrderRepository = courseOrderRepository;
    }

    /**
     * Save a courseOrder.
     *
     * @param courseOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public CourseOrder save(CourseOrder courseOrder) {
        log.debug("Request to save CourseOrder : {}", courseOrder);
        CourseOrder result = courseOrderRepository.save(courseOrder);
        return result;
    }

    /**
     *  Get all the courseOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseOrder> findAll(Pageable pageable) {
        log.debug("Request to get all CourseOrders");
        Page<CourseOrder> result = courseOrderRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one courseOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CourseOrder findOne(Long id) {
        log.debug("Request to get CourseOrder : {}", id);
        CourseOrder courseOrder = courseOrderRepository.findOne(id);
        return courseOrder;
    }

    /**
     *  Delete the  courseOrder by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseOrder : {}", id);
        courseOrderRepository.delete(id);
    }

    @Override
    public List<CourseOrder> findCourseByUser(WechatUser user, String status) throws Exception {
        return courseOrderRepository.findCourseOrdersByUserAndPayStatus(user,status);
    }

    public List<CourseOrder> findOrderByCourseUser(WechatUser user, Course course, String status){
        List<CourseOrder> result=courseOrderRepository.findCourseOrdersByUserAndPayStatusAndCourse(user,status,course);
        return result;
    }

    @Override
    public List<CourseOrder> findMyCourseOrder(WechatUser wechatUser) {
        try{
            log.debug("获取"+wechatUser+"课程购买记录");
            return courseOrderRepository.findByUser(wechatUser);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
