package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.ExhibitionArtwork;
import com.alienlab.ziranli.service.ExhibitionArtworkService;
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
 * REST controller for managing ExhibitionArtwork.
 */
@RestController
@RequestMapping("/api")
public class ExhibitionArtworkResource {

    private final Logger log = LoggerFactory.getLogger(ExhibitionArtworkResource.class);

    private static final String ENTITY_NAME = "exhibitionArtwork";
        
    private final ExhibitionArtworkService exhibitionArtworkService;

    public ExhibitionArtworkResource(ExhibitionArtworkService exhibitionArtworkService) {
        this.exhibitionArtworkService = exhibitionArtworkService;
    }

    /**
     * POST  /exhibition-artworks : Create a new exhibitionArtwork.
     *
     * @param exhibitionArtwork the exhibitionArtwork to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exhibitionArtwork, or with status 400 (Bad Request) if the exhibitionArtwork has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exhibition-artworks")
    @Timed
    public ResponseEntity<ExhibitionArtwork> createExhibitionArtwork(@RequestBody ExhibitionArtwork exhibitionArtwork) throws URISyntaxException {
        log.debug("REST request to save ExhibitionArtwork : {}", exhibitionArtwork);
        if (exhibitionArtwork.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new exhibitionArtwork cannot already have an ID")).body(null);
        }
        ExhibitionArtwork result = exhibitionArtworkService.save(exhibitionArtwork);
        return ResponseEntity.created(new URI("/api/exhibition-artworks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exhibition-artworks : Updates an existing exhibitionArtwork.
     *
     * @param exhibitionArtwork the exhibitionArtwork to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exhibitionArtwork,
     * or with status 400 (Bad Request) if the exhibitionArtwork is not valid,
     * or with status 500 (Internal Server Error) if the exhibitionArtwork couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exhibition-artworks")
    @Timed
    public ResponseEntity<ExhibitionArtwork> updateExhibitionArtwork(@RequestBody ExhibitionArtwork exhibitionArtwork) throws URISyntaxException {
        log.debug("REST request to update ExhibitionArtwork : {}", exhibitionArtwork);
        if (exhibitionArtwork.getId() == null) {
            return createExhibitionArtwork(exhibitionArtwork);
        }
        ExhibitionArtwork result = exhibitionArtworkService.save(exhibitionArtwork);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exhibitionArtwork.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exhibition-artworks : get all the exhibitionArtworks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of exhibitionArtworks in body
     */
    @GetMapping("/exhibition-artworks")
    @Timed
    public ResponseEntity<List<ExhibitionArtwork>> getAllExhibitionArtworks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ExhibitionArtworks");
        Page<ExhibitionArtwork> page = exhibitionArtworkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exhibition-artworks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exhibition-artworks/:id : get the "id" exhibitionArtwork.
     *
     * @param id the id of the exhibitionArtwork to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exhibitionArtwork, or with status 404 (Not Found)
     */
    @GetMapping("/exhibition-artworks/{id}")
    @Timed
    public ResponseEntity<ExhibitionArtwork> getExhibitionArtwork(@PathVariable Long id) {
        log.debug("REST request to get ExhibitionArtwork : {}", id);
        ExhibitionArtwork exhibitionArtwork = exhibitionArtworkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(exhibitionArtwork));
    }

    /**
     * DELETE  /exhibition-artworks/:id : delete the "id" exhibitionArtwork.
     *
     * @param id the id of the exhibitionArtwork to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exhibition-artworks/{id}")
    @Timed
    public ResponseEntity<Void> deleteExhibitionArtwork(@PathVariable Long id) {
        log.debug("REST request to delete ExhibitionArtwork : {}", id);
        exhibitionArtworkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
