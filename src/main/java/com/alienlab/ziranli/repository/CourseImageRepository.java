package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.domain.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 鸠小浅 on 2017/10/14.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseImageRepository extends JpaRepository<CourseImage,Long> {
    List<CourseImage> findCourseImagesByCourse(Course course);
}
