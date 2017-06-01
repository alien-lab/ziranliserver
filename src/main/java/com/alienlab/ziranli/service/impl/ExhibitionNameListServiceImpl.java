package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.ExhibitionNameListService;
import com.alienlab.ziranli.domain.ExhibitionNameList;
import com.alienlab.ziranli.repository.ExhibitionNameListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ExhibitionNameList.
 */
@Service
@Transactional
public class ExhibitionNameListServiceImpl implements ExhibitionNameListService{

    private final Logger log = LoggerFactory.getLogger(ExhibitionNameListServiceImpl.class);
    
    private final ExhibitionNameListRepository exhibitionNameListRepository;

    public ExhibitionNameListServiceImpl(ExhibitionNameListRepository exhibitionNameListRepository) {
        this.exhibitionNameListRepository = exhibitionNameListRepository;
    }

    /**
     * Save a exhibitionNameList.
     *
     * @param exhibitionNameList the entity to save
     * @return the persisted entity
     */
    @Override
    public ExhibitionNameList save(ExhibitionNameList exhibitionNameList) {
        log.debug("Request to save ExhibitionNameList : {}", exhibitionNameList);
        ExhibitionNameList result = exhibitionNameListRepository.save(exhibitionNameList);
        return result;
    }

    /**
     *  Get all the exhibitionNameLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExhibitionNameList> findAll(Pageable pageable) {
        log.debug("Request to get all ExhibitionNameLists");
        Page<ExhibitionNameList> result = exhibitionNameListRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one exhibitionNameList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExhibitionNameList findOne(Long id) {
        log.debug("Request to get ExhibitionNameList : {}", id);
        ExhibitionNameList exhibitionNameList = exhibitionNameListRepository.findOne(id);
        return exhibitionNameList;
    }

    /**
     *  Delete the  exhibitionNameList by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExhibitionNameList : {}", id);
        exhibitionNameListRepository.delete(id);
    }
}
