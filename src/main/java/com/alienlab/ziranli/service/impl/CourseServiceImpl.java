package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.CourseService;
import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService{

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Save a course.
     *
     * @param course the entity to save
     * @return the persisted entity
     */
    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        Course result = courseRepository.save(course);
        return result;
    }

    /**
     *  Get all the courses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        Page<Course> result = courseRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one course by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Course findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        Course course = courseRepository.findOne(id);
        return course;
    }

    /**
     *  Delete the  course by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.delete(id);
    }
    @Override
    public List getAllOnliveRooms(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String time=ZonedDateTime.now().format(formatter);
        String sql="select a.* from wx_onlive_broadcasting a where 1=1 and not exists(select 1 from `course_onlive_relation` b where a.bc_no=b.`onlive_id`) order by a.bc_cttime desc";
        return jdbcTemplate.queryForList(sql);
    }
    @Override
    public Map getCourseOnlive(Long courseId){
        String sql="select a.* from wx_onlive_broadcasting a where exists(select 1 from `course_onlive_relation` b where a.bc_no=b.`onlive_id` and b.`course_id`="+courseId+")";
        try{
            return jdbcTemplate.queryForMap(sql);
        }catch (Exception e){
            return null;
        }

    }
    @Override
    public boolean delCourseOnlive(Long courseId,String onliveId){
        String sql="delete from course_onlive_relation where onlive_id="+onliveId+" and course_id="+courseId;
        try{
            jdbcTemplate.execute(sql);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean addCourseOnlive(Long courseId,String onliveId){
        String sql="insert into course_onlive_relation(`course_id`,`onlive_id`) values("+courseId+","+onliveId+")";
        try{
            jdbcTemplate.execute(sql);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
