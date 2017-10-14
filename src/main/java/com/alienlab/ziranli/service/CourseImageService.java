package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.CourseImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by 鸠小浅 on 2017/10/14.
 */
public interface CourseImageService {
    /**
     * Save a courseImage.
     *
     * @param courseImage the entity to save
     * @return the persisted entity
     */
    CourseImage save(CourseImage courseImage);

    /**
     *  Get all the courseImages.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CourseImage> findAll(Pageable pageable);

    /**
     *  Get the "id" courseImage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CourseImage findOne(Long id);

    /**
     *  Delete the "id" courseImage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
