package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.CourseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CourseType.
 */
public interface CourseTypeService {

    /**
     * Save a courseType.
     *
     * @param courseType the entity to save
     * @return the persisted entity
     */
    CourseType save(CourseType courseType);

    /**
     *  Get all the courseTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseType> findAll(Pageable pageable);

    /**
     *  Get the "id" courseType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseType findOne(Long id);

    /**
     *  Delete the "id" courseType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
