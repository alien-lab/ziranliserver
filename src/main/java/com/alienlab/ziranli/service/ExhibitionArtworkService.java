package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.Exhibition;
import com.alienlab.ziranli.domain.ExhibitionArtwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ExhibitionArtwork.
 */
public interface ExhibitionArtworkService {

    /**
     * Save a exhibitionArtwork.
     *
     * @param exhibitionArtwork the entity to save
     * @return the persisted entity
     */
    ExhibitionArtwork save(ExhibitionArtwork exhibitionArtwork);

    /**
     * Get all the exhibitionArtworks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExhibitionArtwork> findAll(Pageable pageable);

    /**
     * Get the "id" exhibitionArtwork.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ExhibitionArtwork findOne(Long id);

    /**
     * Delete the "id" exhibitionArtwork.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<ExhibitionArtwork> findByExhibition(Long exhibitionId) throws Exception;

}
