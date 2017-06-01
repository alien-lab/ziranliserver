package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.ShareLog;
import com.alienlab.ziranli.service.ShareLogService;
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
 * REST controller for managing ShareLog.
 */
@RestController
@RequestMapping("/api")
public class ShareLogResource {

    private final Logger log = LoggerFactory.getLogger(ShareLogResource.class);

    private static final String ENTITY_NAME = "shareLog";
        
    private final ShareLogService shareLogService;

    public ShareLogResource(ShareLogService shareLogService) {
        this.shareLogService = shareLogService;
    }

    /**
     * POST  /share-logs : Create a new shareLog.
     *
     * @param shareLog the shareLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shareLog, or with status 400 (Bad Request) if the shareLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/share-logs")
    @Timed
    public ResponseEntity<ShareLog> createShareLog(@RequestBody ShareLog shareLog) throws URISyntaxException {
        log.debug("REST request to save ShareLog : {}", shareLog);
        if (shareLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new shareLog cannot already have an ID")).body(null);
        }
        ShareLog result = shareLogService.save(shareLog);
        return ResponseEntity.created(new URI("/api/share-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /share-logs : Updates an existing shareLog.
     *
     * @param shareLog the shareLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shareLog,
     * or with status 400 (Bad Request) if the shareLog is not valid,
     * or with status 500 (Internal Server Error) if the shareLog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/share-logs")
    @Timed
    public ResponseEntity<ShareLog> updateShareLog(@RequestBody ShareLog shareLog) throws URISyntaxException {
        log.debug("REST request to update ShareLog : {}", shareLog);
        if (shareLog.getId() == null) {
            return createShareLog(shareLog);
        }
        ShareLog result = shareLogService.save(shareLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shareLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /share-logs : get all the shareLogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shareLogs in body
     */
    @GetMapping("/share-logs")
    @Timed
    public ResponseEntity<List<ShareLog>> getAllShareLogs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ShareLogs");
        Page<ShareLog> page = shareLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/share-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /share-logs/:id : get the "id" shareLog.
     *
     * @param id the id of the shareLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shareLog, or with status 404 (Not Found)
     */
    @GetMapping("/share-logs/{id}")
    @Timed
    public ResponseEntity<ShareLog> getShareLog(@PathVariable Long id) {
        log.debug("REST request to get ShareLog : {}", id);
        ShareLog shareLog = shareLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shareLog));
    }

    /**
     * DELETE  /share-logs/:id : delete the "id" shareLog.
     *
     * @param id the id of the shareLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/share-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteShareLog(@PathVariable Long id) {
        log.debug("REST request to delete ShareLog : {}", id);
        shareLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
