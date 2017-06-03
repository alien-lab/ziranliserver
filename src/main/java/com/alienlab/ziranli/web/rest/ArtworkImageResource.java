package com.alienlab.ziranli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.ziranli.domain.ArtworkImage;
import com.alienlab.ziranli.service.ArtworkImageService;
import com.alienlab.ziranli.web.rest.util.HeaderUtil;
import com.alienlab.ziranli.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
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
 * REST controller for managing ArtworkImage.
 */
@RestController
@RequestMapping("/api")
public class ArtworkImageResource {

    private final Logger log = LoggerFactory.getLogger(ArtworkImageResource.class);

    private static final String ENTITY_NAME = "artworkImage";

    private final ArtworkImageService artworkImageService;

    public ArtworkImageResource(ArtworkImageService artworkImageService) {
        this.artworkImageService = artworkImageService;
    }

    @ApiOperation(value="添加艺术品图册图片")
    @PostMapping("/artwork-images")
    @Timed
    public ResponseEntity<ArtworkImage> createArtworkImage(@RequestBody ArtworkImage artworkImage) throws URISyntaxException {
        log.debug("REST request to save ArtworkImage : {}", artworkImage);
        if (artworkImage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new artworkImage cannot already have an ID")).body(null);
        }
        ArtworkImage result = artworkImageService.save(artworkImage);
        return ResponseEntity.created(new URI("/api/artwork-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artwork-images : Updates an existing artworkImage.
     *
     * @param artworkImage the artworkImage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artworkImage,
     * or with status 400 (Bad Request) if the artworkImage is not valid,
     * or with status 500 (Internal Server Error) if the artworkImage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/artwork-images")
    @Timed
    public ResponseEntity<ArtworkImage> updateArtworkImage(@RequestBody ArtworkImage artworkImage) throws URISyntaxException {
        log.debug("REST request to update ArtworkImage : {}", artworkImage);
        if (artworkImage.getId() == null) {
            return createArtworkImage(artworkImage);
        }
        ArtworkImage result = artworkImageService.save(artworkImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, artworkImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artwork-images : get all the artworkImages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of artworkImages in body
     */
    @GetMapping("/artwork-images")
    @Timed
    public ResponseEntity<List<ArtworkImage>> getAllArtworkImages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ArtworkImages");
        Page<ArtworkImage> page = artworkImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artwork-images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artwork-images/:id : get the "id" artworkImage.
     *
     * @param id the id of the artworkImage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artworkImage, or with status 404 (Not Found)
     */
    @GetMapping("/artwork-images/{id}")
    @Timed
    public ResponseEntity<ArtworkImage> getArtworkImage(@PathVariable Long id) {
        log.debug("REST request to get ArtworkImage : {}", id);
        ArtworkImage artworkImage = artworkImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(artworkImage));
    }

    /**
     * DELETE  /artwork-images/:id : delete the "id" artworkImage.
     *
     * @param id the id of the artworkImage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/artwork-images/{id}")
    @Timed
    public ResponseEntity<Void> deleteArtworkImage(@PathVariable Long id) {
        log.debug("REST request to delete ArtworkImage : {}", id);
        artworkImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
