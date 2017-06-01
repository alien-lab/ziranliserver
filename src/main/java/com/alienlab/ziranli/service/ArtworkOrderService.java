package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.ArtworkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ArtworkOrder.
 */
public interface ArtworkOrderService {

    /**
     * Save a artworkOrder.
     *
     * @param artworkOrder the entity to save
     * @return the persisted entity
     */
    ArtworkOrder save(ArtworkOrder artworkOrder);

    /**
     *  Get all the artworkOrders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ArtworkOrder> findAll(Pageable pageable);

    /**
     *  Get the "id" artworkOrder.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ArtworkOrder findOne(Long id);

    /**
     *  Delete the "id" artworkOrder.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
