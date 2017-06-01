package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.service.CourseOrderService;
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
 * REST controller for managing CourseOrder.
 */
@RestController
@RequestMapping("/api")
public class CourseOrderResource {

    private final Logger log = LoggerFactory.getLogger(CourseOrderResource.class);

    private static final String ENTITY_NAME = "courseOrder";
        
    private final CourseOrderService courseOrderService;

    public CourseOrderResource(CourseOrderService courseOrderService) {
        this.courseOrderService = courseOrderService;
    }

    /**
     * POST  /course-orders : Create a new courseOrder.
     *
     * @param courseOrder the courseOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseOrder, or with status 400 (Bad Request) if the courseOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-orders")
    @Timed
    public ResponseEntity<CourseOrder> createCourseOrder(@RequestBody CourseOrder courseOrder) throws URISyntaxException {
        log.debug("REST request to save CourseOrder : {}", courseOrder);
        if (courseOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseOrder cannot already have an ID")).body(null);
        }
        CourseOrder result = courseOrderService.save(courseOrder);
        return ResponseEntity.created(new URI("/api/course-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-orders : Updates an existing courseOrder.
     *
     * @param courseOrder the courseOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseOrder,
     * or with status 400 (Bad Request) if the courseOrder is not valid,
     * or with status 500 (Internal Server Error) if the courseOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-orders")
    @Timed
    public ResponseEntity<CourseOrder> updateCourseOrder(@RequestBody CourseOrder courseOrder) throws URISyntaxException {
        log.debug("REST request to update CourseOrder : {}", courseOrder);
        if (courseOrder.getId() == null) {
            return createCourseOrder(courseOrder);
        }
        CourseOrder result = courseOrderService.save(courseOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-orders : get all the courseOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseOrders in body
     */
    @GetMapping("/course-orders")
    @Timed
    public ResponseEntity<List<CourseOrder>> getAllCourseOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseOrders");
        Page<CourseOrder> page = courseOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-orders/:id : get the "id" courseOrder.
     *
     * @param id the id of the courseOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseOrder, or with status 404 (Not Found)
     */
    @GetMapping("/course-orders/{id}")
    @Timed
    public ResponseEntity<CourseOrder> getCourseOrder(@PathVariable Long id) {
        log.debug("REST request to get CourseOrder : {}", id);
        CourseOrder courseOrder = courseOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseOrder));
    }

    /**
     * DELETE  /course-orders/:id : delete the "id" courseOrder.
     *
     * @param id the id of the courseOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseOrder(@PathVariable Long id) {
        log.debug("REST request to delete CourseOrder : {}", id);
        courseOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
