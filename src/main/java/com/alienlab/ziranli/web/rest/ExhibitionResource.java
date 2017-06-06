package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.Exhibition;
import com.alienlab.ziranli.service.ExhibitionService;
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
 * REST controller for managing Exhibition.
 */
@RestController
@RequestMapping("/api")
public class ExhibitionResource {

    private final Logger log = LoggerFactory.getLogger(ExhibitionResource.class);

    private static final String ENTITY_NAME = "exhibition";

    private final ExhibitionService exhibitionService;

    public ExhibitionResource(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    /**
     * POST  /exhibitions : Create a new exhibition.
     *
     * @param exhibition the exhibition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exhibition, or with status 400 (Bad Request) if the exhibition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exhibitions")
    @Timed
    public ResponseEntity<Exhibition> createExhibition(@RequestBody Exhibition exhibition) throws URISyntaxException {
        log.debug("REST request to save Exhibition : {}", exhibition);
        System.out.println(exhibition);
        if (exhibition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new exhibition cannot already have an ID")).body(null);
        }
        Exhibition result = exhibitionService.save(exhibition);
        return ResponseEntity.created(new URI("/api/exhibitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exhibitions : Updates an existing exhibition.
     *
     * @param exhibition the exhibition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exhibition,
     * or with status 400 (Bad Request) if the exhibition is not valid,
     * or with status 500 (Internal Server Error) if the exhibition couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exhibitions")
    @Timed
    public ResponseEntity<Exhibition> updateExhibition(@RequestBody Exhibition exhibition) throws URISyntaxException {
        log.debug("REST request to update Exhibition : {}", exhibition);
        if (exhibition.getId() == null) {
            return createExhibition(exhibition);
        }
        Exhibition result = exhibitionService.save(exhibition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exhibition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exhibitions : get all the exhibitions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of exhibitions in body
     */
    @GetMapping("/exhibitions")
    @Timed
    public ResponseEntity<List<Exhibition>> getAllExhibitions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Exhibitions");
        Page<Exhibition> page = exhibitionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exhibitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exhibitions/:id : get the "id" exhibition.
     *
     * @param id the id of the exhibition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exhibition, or with status 404 (Not Found)
     */
    @GetMapping("/exhibitions/{id}")
    @Timed
    public ResponseEntity<Exhibition> getExhibition(@PathVariable Long id) {
        log.debug("REST request to get Exhibition : {}", id);
        Exhibition exhibition = exhibitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(exhibition));
    }

    /**
     * DELETE  /exhibitions/:id : delete the "id" exhibition.
     *
     * @param id the id of the exhibition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exhibitions/{id}")
    @Timed
    public ResponseEntity<Void> deleteExhibition(@PathVariable Long id) {
        log.debug("REST request to delete Exhibition : {}", id);
        exhibitionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
