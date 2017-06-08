package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CourseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseOrderRepository extends JpaRepository<CourseOrder,Long> {
    List<CourseOrder> findByUser(WechatUser wechatUser);
}
