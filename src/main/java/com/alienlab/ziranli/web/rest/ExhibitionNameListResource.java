package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.ExhibitionNameList;
import com.alienlab.ziranli.service.ExhibitionNameListService;
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
 * REST controller for managing ExhibitionNameList.
 */
@RestController
@RequestMapping("/api")
public class ExhibitionNameListResource {

    private final Logger log = LoggerFactory.getLogger(ExhibitionNameListResource.class);

    private static final String ENTITY_NAME = "exhibitionNameList";
        
    private final ExhibitionNameListService exhibitionNameListService;

    public ExhibitionNameListResource(ExhibitionNameListService exhibitionNameListService) {
        this.exhibitionNameListService = exhibitionNameListService;
    }

    /**
     * POST  /exhibition-name-lists : Create a new exhibitionNameList.
     *
     * @param exhibitionNameList the exhibitionNameList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exhibitionNameList, or with status 400 (Bad Request) if the exhibitionNameList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exhibition-name-lists")
    @Timed
    public ResponseEntity<ExhibitionNameList> createExhibitionNameList(@RequestBody ExhibitionNameList exhibitionNameList) throws URISyntaxException {
        log.debug("REST request to save ExhibitionNameList : {}", exhibitionNameList);
        if (exhibitionNameList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new exhibitionNameList cannot already have an ID")).body(null);
        }
        ExhibitionNameList result = exhibitionNameListService.save(exhibitionNameList);
        return ResponseEntity.created(new URI("/api/exhibition-name-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exhibition-name-lists : Updates an existing exhibitionNameList.
     *
     * @param exhibitionNameList the exhibitionNameList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exhibitionNameList,
     * or with status 400 (Bad Request) if the exhibitionNameList is not valid,
     * or with status 500 (Internal Server Error) if the exhibitionNameList couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exhibition-name-lists")
    @Timed
    public ResponseEntity<ExhibitionNameList> updateExhibitionNameList(@RequestBody ExhibitionNameList exhibitionNameList) throws URISyntaxException {
        log.debug("REST request to update ExhibitionNameList : {}", exhibitionNameList);
        if (exhibitionNameList.getId() == null) {
            return createExhibitionNameList(exhibitionNameList);
        }
        ExhibitionNameList result = exhibitionNameListService.save(exhibitionNameList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exhibitionNameList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exhibition-name-lists : get all the exhibitionNameLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of exhibitionNameLists in body
     */
    @GetMapping("/exhibition-name-lists")
    @Timed
    public ResponseEntity<List<ExhibitionNameList>> getAllExhibitionNameLists(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ExhibitionNameLists");
        Page<ExhibitionNameList> page = exhibitionNameListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exhibition-name-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exhibition-name-lists/:id : get the "id" exhibitionNameList.
     *
     * @param id the id of the exhibitionNameList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exhibitionNameList, or with status 404 (Not Found)
     */
    @GetMapping("/exhibition-name-lists/{id}")
    @Timed
    public ResponseEntity<ExhibitionNameList> getExhibitionNameList(@PathVariable Long id) {
        log.debug("REST request to get ExhibitionNameList : {}", id);
        ExhibitionNameList exhibitionNameList = exhibitionNameListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(exhibitionNameList));
    }

    /**
     * DELETE  /exhibition-name-lists/:id : delete the "id" exhibitionNameList.
     *
     * @param id the id of the exhibitionNameList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exhibition-name-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteExhibitionNameList(@PathVariable Long id) {
        log.debug("REST request to delete ExhibitionNameList : {}", id);
        exhibitionNameListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
