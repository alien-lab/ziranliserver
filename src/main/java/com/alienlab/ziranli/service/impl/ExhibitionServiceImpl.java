package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.ExhibitionService;
import com.alienlab.ziranli.domain.Exhibition;
import com.alienlab.ziranli.repository.ExhibitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Exhibition.
 */
@Service
@Transactional
public class ExhibitionServiceImpl implements ExhibitionService{

    private final Logger log = LoggerFactory.getLogger(ExhibitionServiceImpl.class);
    
    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionServiceImpl(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }

    /**
     * Save a exhibition.
     *
     * @param exhibition the entity to save
     * @return the persisted entity
     */
    @Override
    public Exhibition save(Exhibition exhibition) {
        log.debug("Request to save Exhibition : {}", exhibition);
        Exhibition result = exhibitionRepository.save(exhibition);
        return result;
    }

    /**
     *  Get all the exhibitions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Exhibition> findAll(Pageable pageable) {
        log.debug("Request to get all Exhibitions");
        Page<Exhibition> result = exhibitionRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one exhibition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Exhibition findOne(Long id) {
        log.debug("Request to get Exhibition : {}", id);
        Exhibition exhibition = exhibitionRepository.findOne(id);
        return exhibition;
    }

    /**
     *  Delete the  exhibition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exhibition : {}", id);
        exhibitionRepository.delete(id);
    }
}
