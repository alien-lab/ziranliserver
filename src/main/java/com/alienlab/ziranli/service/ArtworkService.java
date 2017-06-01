package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Artwork.
 */
public interface ArtworkService {

    /**
     * Save a artwork.
     *
     * @param artwork the entity to save
     * @return the persisted entity
     */
    Artwork save(Artwork artwork);

    /**
     *  Get all the artworks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Artwork> findAll(Pageable pageable);

    /**
     *  Get the "id" artwork.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Artwork findOne(Long id);

    /**
     *  Delete the "id" artwork.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
