package com.alienlab.ziranli.web.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.alienlab.ziranli.web.rest.util.ExecResult;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.Course;
import com.alienlab.ziranli.service.CourseService;
import com.alienlab.ziranli.web.rest.util.HeaderUtil;
import com.alienlab.ziranli.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Course.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    private final CourseService courseService;

    public CourseResource(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * POST  /courses : Create a new course.
     *
     * @param course the course to create
     * @return the ResponseEntity with status 201 (Created) and with body the new course, or with status 400 (Bad Request) if the course has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courses")
    @Timed
    public ResponseEntity<Course> createCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new course cannot already have an ID")).body(null);
        }
        Course result = courseService.save(course);
        if(course.getOnliveId()!=null&&course.getOnliveId().length()>0){
            courseService.addCourseOnlive(result.getId(),course.getOnliveId());
        }
        return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courses : Updates an existing course.
     *
     * @param course the course to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated course,
     * or with status 400 (Bad Request) if the course is not valid,
     * or with status 500 (Internal Server Error) if the course couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courses")
    @Timed
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getId() == null) {
            return createCourse(course);
        }
        Course result = courseService.save(course);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, course.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courses : get all the courses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courses in body
     */
    @GetMapping("/courses")
    @Timed
    public ResponseEntity<List<Course>> getAllCourses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Courses");
        Page<Course> page = courseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /courses/:id : get the "id" course.
     *
     * @param id the id of the course to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the course, or with status 404 (Not Found)
     */
    @GetMapping("/courses/{id}")
    @Timed
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Course course = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(course));
    }

    /**
     * DELETE  /courses/:id : delete the "id" course.
     *
     * @param id the id of the course to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/courses/onlive")
    @Timed
    public ResponseEntity loadOnliveRooms(){
        List result=courseService.getAllOnliveRooms();
        return ResponseEntity.ok().body(result);
    }
    @GetMapping("/courses/onlive/{courseId}")
    public ResponseEntity loadCourseOnlive(@PathVariable Long courseId){
        Map result=courseService.getCourseOnlive(courseId);
        if(result!=null){
            return ResponseEntity.ok().body(result);
        }else{
            return ResponseEntity.ok().body(new JSONObject());
        }

    }

    @PostMapping("/courses/onlive")
    public ResponseEntity loadCourseOnlive(@RequestBody Map map){
        Long courseId=TypeUtils.castToLong(map.get("courseId"));
        String onliveId=TypeUtils.castToString(map.get("onliveId"));
        Map validatemap=courseService.getCourseOnlive(courseId);
        if(validatemap!=null&&validatemap.containsKey("bc_no")){
            if(onliveId.equals(TypeUtils.castToString(validatemap.get("bc_no")))){
                ExecResult er=new ExecResult(false,"已经绑定了该直播内容");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }
        boolean result=courseService.addCourseOnlive(courseId,onliveId );
        ExecResult er=new ExecResult(result,(result?"直播关联成功":"直播关联失败"));
        return ResponseEntity.ok().body(er);
    }

    @DeleteMapping("/courses/onlive/{courseId}/{onliveId}")
    public ResponseEntity loadCourseOnlive(@PathVariable Long courseId,@PathVariable String onliveId){
        boolean result=courseService.delCourseOnlive(courseId, onliveId);
        ExecResult er=new ExecResult(result,(result?"解除直播关联成功":"解除直播关联失败"));
        return ResponseEntity.ok().body(er);
    }

}
