package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.ArtworkOrderService;
import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.repository.ArtworkOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ArtworkOrder.
 */
@Service
@Transactional
public class ArtworkOrderServiceImpl implements ArtworkOrderService{

    private final Logger log = LoggerFactory.getLogger(ArtworkOrderServiceImpl.class);
    
    private final ArtworkOrderRepository artworkOrderRepository;

    public ArtworkOrderServiceImpl(ArtworkOrderRepository artworkOrderRepository) {
        this.artworkOrderRepository = artworkOrderRepository;
    }

    /**
     * Save a artworkOrder.
     *
     * @param artworkOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public ArtworkOrder save(ArtworkOrder artworkOrder) {
        log.debug("Request to save ArtworkOrder : {}", artworkOrder);
        ArtworkOrder result = artworkOrderRepository.save(artworkOrder);
        return result;
    }

    /**
     *  Get all the artworkOrders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArtworkOrder> findAll(Pageable pageable) {
        log.debug("Request to get all ArtworkOrders");
        Page<ArtworkOrder> result = artworkOrderRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one artworkOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ArtworkOrder findOne(Long id) {
        log.debug("Request to get ArtworkOrder : {}", id);
        ArtworkOrder artworkOrder = artworkOrderRepository.findOne(id);
        return artworkOrder;
    }

    /**
     *  Delete the  artworkOrder by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArtworkOrder : {}", id);
        artworkOrderRepository.delete(id);
    }
}