package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.CourseOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseOrderRepository extends JpaRepository<CourseOrder,Long> {

}
