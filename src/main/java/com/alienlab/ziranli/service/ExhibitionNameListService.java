package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.ExhibitionNameList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ExhibitionNameList.
 */
public interface ExhibitionNameListService {

    /**
     * Save a exhibitionNameList.
     *
     * @param exhibitionNameList the entity to save
     * @return the persisted entity
     */
    ExhibitionNameList save(ExhibitionNameList exhibitionNameList);

    /**
     *  Get all the exhibitionNameLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExhibitionNameList> findAll(Pageable pageable);

    /**
     *  Get the "id" exhibitionNameList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExhibitionNameList findOne(Long id);

    /**
     *  Delete the "id" exhibitionNameList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
