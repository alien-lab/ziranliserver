package com.alienlab.ziranli.web.rest;

import com.alienlab.ziranli.domain.CourseImage;
import com.alienlab.ziranli.service.CourseImageService;
import com.alienlab.ziranli.web.rest.util.HeaderUtil;
import com.alienlab.ziranli.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * Created by 鸠小浅 on 2017/10/14.
 */

@RestController
@RequestMapping("/api")
public class CourseImageResource {

    private final Logger log = LoggerFactory.getLogger(CourseImageResource.class);

    private static final String ENTITY_NAME = "courseImage";

    private final CourseImageService courseImageService;

    public CourseImageResource(CourseImageService courseImageService) {
        this.courseImageService = courseImageService;
    }

    @ApiOperation(value="添加课程图册图片")
    @PostMapping("/course-images")
    @Timed
    public ResponseEntity<CourseImage> createCourseImage(@RequestBody CourseImage courseImage) throws URISyntaxException {
        log.debug("REST request to save CourseImage : {}", courseImage);
        if (courseImage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseImage cannot already have an ID")).body(null);
        }
        CourseImage result = courseImageService.save(courseImage);
        return ResponseEntity.created(new URI("/api/course-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-images : Updates an existing courseImage.
     *
     * @param courseImage the courseImage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseImage,
     * or with status 400 (Bad Request) if the courseImage is not valid,
     * or with status 500 (Internal Server Error) if the courseImage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-images")
    @Timed
    public ResponseEntity<CourseImage> updateCourseImage(@RequestBody CourseImage courseImage) throws URISyntaxException {
        log.debug("REST request to update CourseImage : {}", courseImage);
        if (courseImage.getId() == null) {
            return createCourseImage(courseImage);
        }
        CourseImage result = courseImageService.save(courseImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-images : get all the courseImages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseImages in body
     */
    @GetMapping("/course-images")
    @Timed
    public ResponseEntity<List<CourseImage>> getAllCourseImages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseImages");
        Page<CourseImage> page = courseImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-images/:id : get the "id" courseImage.
     *
     * @param id the id of the courseImage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseImage, or with status 404 (Not Found)
     */
    @GetMapping("/course-images/{id}")
    @Timed
    public ResponseEntity<CourseImage> getCourseImage(@PathVariable Long id) {
        log.debug("REST request to get CourseImage : {}", id);
        CourseImage courseImage = courseImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseImage));
    }

    /**
     * DELETE  /course-images/:id : delete the "id" courseImage.
     *
     * @param id the id of the courseImage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-images/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseImage(@PathVariable Long id) {
        log.debug("REST request to delete CourseImage : {}", id);
        courseImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
