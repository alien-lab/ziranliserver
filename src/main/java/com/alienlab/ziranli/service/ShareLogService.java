package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.ShareLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ShareLog.
 */
public interface ShareLogService {

    /**
     * Save a shareLog.
     *
     * @param shareLog the entity to save
     * @return the persisted entity
     */
    ShareLog save(ShareLog shareLog);

    /**
     *  Get all the shareLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ShareLog> findAll(Pageable pageable);

    /**
     *  Get the "id" shareLog.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ShareLog findOne(Long id);

    /**
     *  Delete the "id" shareLog.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<ShareLog> getCourseShareLog(Long courseId, String openid) throws Exception;
}
