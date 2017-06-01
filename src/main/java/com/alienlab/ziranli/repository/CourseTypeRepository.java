package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.CourseType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseTypeRepository extends JpaRepository<CourseType,Long> {

}
