package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.ArtworkImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ArtworkImage.
 */
public interface ArtworkImageService {

    /**
     * Save a artworkImage.
     *
     * @param artworkImage the entity to save
     * @return the persisted entity
     */
    ArtworkImage save(ArtworkImage artworkImage);

    /**
     *  Get all the artworkImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ArtworkImage> findAll(Pageable pageable);

    /**
     *  Get the "id" artworkImage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ArtworkImage findOne(Long id);

    /**
     *  Delete the "id" artworkImage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
