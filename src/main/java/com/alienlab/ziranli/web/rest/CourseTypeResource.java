package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.CourseType;
import com.alienlab.ziranli.service.CourseTypeService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CourseType.
 */
@RestController
@RequestMapping("/api")
public class CourseTypeResource {

    private final Logger log = LoggerFactory.getLogger(CourseTypeResource.class);

    private static final String ENTITY_NAME = "courseType";
        
    private final CourseTypeService courseTypeService;

    public CourseTypeResource(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    /**
     * POST  /course-types : Create a new courseType.
     *
     * @param courseType the courseType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseType, or with status 400 (Bad Request) if the courseType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-types")
    @Timed
    public ResponseEntity<CourseType> createCourseType(@RequestBody CourseType courseType) throws URISyntaxException {
        log.debug("REST request to save CourseType : {}", courseType);
        if (courseType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseType cannot already have an ID")).body(null);
        }
        CourseType result = courseTypeService.save(courseType);
        return ResponseEntity.created(new URI("/api/course-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-types : Updates an existing courseType.
     *
     * @param courseType the courseType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseType,
     * or with status 400 (Bad Request) if the courseType is not valid,
     * or with status 500 (Internal Server Error) if the courseType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-types")
    @Timed
    public ResponseEntity<CourseType> updateCourseType(@RequestBody CourseType courseType) throws URISyntaxException {
        log.debug("REST request to update CourseType : {}", courseType);
        if (courseType.getId() == null) {
            return createCourseType(courseType);
        }
        CourseType result = courseTypeService.save(courseType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-types : get all the courseTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseTypes in body
     */
    @GetMapping("/course-types")
    @Timed
    public ResponseEntity<List<CourseType>> getAllCourseTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseTypes");
        Page<CourseType> page = courseTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-types/:id : get the "id" courseType.
     *
     * @param id the id of the courseType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseType, or with status 404 (Not Found)
     */
    @GetMapping("/course-types/{id}")
    @Timed
    public ResponseEntity<CourseType> getCourseType(@PathVariable Long id) {
        log.debug("REST request to get CourseType : {}", id);
        CourseType courseType = courseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseType));
    }

    /**
     * DELETE  /course-types/:id : delete the "id" courseType.
     *
     * @param id the id of the courseType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseType(@PathVariable Long id) {
        log.debug("REST request to delete CourseType : {}", id);
        courseTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
