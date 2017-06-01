package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Exhibition.
 */
public interface ExhibitionService {

    /**
     * Save a exhibition.
     *
     * @param exhibition the entity to save
     * @return the persisted entity
     */
    Exhibition save(Exhibition exhibition);

    /**
     *  Get all the exhibitions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Exhibition> findAll(Pageable pageable);

    /**
     *  Get the "id" exhibition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Exhibition findOne(Long id);

    /**
     *  Delete the "id" exhibition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
