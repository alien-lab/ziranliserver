package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import com.alienlab.ziranli.web.wechat.controller.Wechat;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CourseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseOrderRepository extends JpaRepository<CourseOrder,Long> {
    List<CourseOrder> findCourseOrdersByUserAndPayStatus(WechatUser user, String status);

    List<CourseOrder> findCourseOrdersByUserAndPayStatusAndCourse(WechatUser user, String status,Course course);

    List<CourseOrder> findByUser(WechatUser wechatUser);
}
