package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.CourseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CourseOrder.
 */
public interface CourseOrderService {

    /**
     * Save a courseOrder.
     *
     * @param courseOrder the entity to save
     * @return the persisted entity
     */
    CourseOrder save(CourseOrder courseOrder);

    /**
     *  Get all the courseOrders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseOrder> findAll(Pageable pageable);

    /**
     *  Get the "id" courseOrder.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseOrder findOne(Long id);

    /**
     *  Delete the "id" courseOrder.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
