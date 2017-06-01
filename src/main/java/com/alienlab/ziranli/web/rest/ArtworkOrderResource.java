package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.service.ArtworkOrderService;
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
 * REST controller for managing ArtworkOrder.
 */
@RestController
@RequestMapping("/api")
public class ArtworkOrderResource {

    private final Logger log = LoggerFactory.getLogger(ArtworkOrderResource.class);

    private static final String ENTITY_NAME = "artworkOrder";
        
    private final ArtworkOrderService artworkOrderService;

    public ArtworkOrderResource(ArtworkOrderService artworkOrderService) {
        this.artworkOrderService = artworkOrderService;
    }

    /**
     * POST  /artwork-orders : Create a new artworkOrder.
     *
     * @param artworkOrder the artworkOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new artworkOrder, or with status 400 (Bad Request) if the artworkOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/artwork-orders")
    @Timed
    public ResponseEntity<ArtworkOrder> createArtworkOrder(@RequestBody ArtworkOrder artworkOrder) throws URISyntaxException {
        log.debug("REST request to save ArtworkOrder : {}", artworkOrder);
        if (artworkOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new artworkOrder cannot already have an ID")).body(null);
        }
        ArtworkOrder result = artworkOrderService.save(artworkOrder);
        return ResponseEntity.created(new URI("/api/artwork-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artwork-orders : Updates an existing artworkOrder.
     *
     * @param artworkOrder the artworkOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artworkOrder,
     * or with status 400 (Bad Request) if the artworkOrder is not valid,
     * or with status 500 (Internal Server Error) if the artworkOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/artwork-orders")
    @Timed
    public ResponseEntity<ArtworkOrder> updateArtworkOrder(@RequestBody ArtworkOrder artworkOrder) throws URISyntaxException {
        log.debug("REST request to update ArtworkOrder : {}", artworkOrder);
        if (artworkOrder.getId() == null) {
            return createArtworkOrder(artworkOrder);
        }
        ArtworkOrder result = artworkOrderService.save(artworkOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, artworkOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artwork-orders : get all the artworkOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of artworkOrders in body
     */
    @GetMapping("/artwork-orders")
    @Timed
    public ResponseEntity<List<ArtworkOrder>> getAllArtworkOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ArtworkOrders");
        Page<ArtworkOrder> page = artworkOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artwork-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artwork-orders/:id : get the "id" artworkOrder.
     *
     * @param id the id of the artworkOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artworkOrder, or with status 404 (Not Found)
     */
    @GetMapping("/artwork-orders/{id}")
    @Timed
    public ResponseEntity<ArtworkOrder> getArtworkOrder(@PathVariable Long id) {
        log.debug("REST request to get ArtworkOrder : {}", id);
        ArtworkOrder artworkOrder = artworkOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(artworkOrder));
    }

    /**
     * DELETE  /artwork-orders/:id : delete the "id" artworkOrder.
     *
     * @param id the id of the artworkOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/artwork-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteArtworkOrder(@PathVariable Long id) {
        log.debug("REST request to delete ArtworkOrder : {}", id);
        artworkOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
