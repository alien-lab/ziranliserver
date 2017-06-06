package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.domain.Exhibition;
import com.alienlab.ziranli.repository.ExhibitionRepository;
import com.alienlab.ziranli.service.ExhibitionArtworkService;
import com.alienlab.ziranli.domain.ExhibitionArtwork;
import com.alienlab.ziranli.repository.ExhibitionArtworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ExhibitionArtwork.
 */
@Service
@Transactional
public class ExhibitionArtworkServiceImpl implements ExhibitionArtworkService {

    private final Logger log = LoggerFactory.getLogger(ExhibitionArtworkServiceImpl.class);

    private final ExhibitionArtworkRepository exhibitionArtworkRepository;
    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionArtworkServiceImpl(ExhibitionArtworkRepository exhibitionArtworkRepository, ExhibitionRepository exhibitionRepository) {
        this.exhibitionArtworkRepository = exhibitionArtworkRepository;
        this.exhibitionRepository = exhibitionRepository;
    }

    /**
     * Save a exhibitionArtwork.
     *
     * @param exhibitionArtwork the entity to save
     * @return the persisted entity
     */
    @Override
    public ExhibitionArtwork save(ExhibitionArtwork exhibitionArtwork) {
        log.debug("Request to save ExhibitionArtwork : {}", exhibitionArtwork);
        ExhibitionArtwork result = exhibitionArtworkRepository.save(exhibitionArtwork);
        return result;
    }

    /**
     * Get all the exhibitionArtworks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExhibitionArtwork> findAll(Pageable pageable) {
        log.debug("Request to get all ExhibitionArtworks");
        Page<ExhibitionArtwork> result = exhibitionArtworkRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one exhibitionArtwork by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExhibitionArtwork findOne(Long id) {
        log.debug("Request to get ExhibitionArtwork : {}", id);
        ExhibitionArtwork exhibitionArtwork = exhibitionArtworkRepository.findOne(id);
        return exhibitionArtwork;
    }

    /**
     * Delete the  exhibitionArtwork by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExhibitionArtwork : {}", id);
        exhibitionArtworkRepository.delete(id);
    }

    @Override
    public List<ExhibitionArtwork> findByExhibition(Long exhibitionId) throws Exception {
        Exhibition exhibition = exhibitionRepository.findOne(exhibitionId);
        if (exhibition == null) {
            throw new Exception("未找到编码为" + exhibitionId + "的展览");
        }
        return exhibitionArtworkRepository.findByExhibition(exhibition);
    }

}
