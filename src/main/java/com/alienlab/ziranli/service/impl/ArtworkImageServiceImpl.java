package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.ArtworkImageService;
import com.alienlab.ziranli.domain.ArtworkImage;
import com.alienlab.ziranli.repository.ArtworkImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ArtworkImage.
 */
@Service
@Transactional
public class ArtworkImageServiceImpl implements ArtworkImageService{

    private final Logger log = LoggerFactory.getLogger(ArtworkImageServiceImpl.class);

    private final ArtworkImageRepository artworkImageRepository;

    public ArtworkImageServiceImpl(ArtworkImageRepository artworkImageRepository) {
        this.artworkImageRepository = artworkImageRepository;
    }

    /**
     * Save a artworkImage.
     *
     * @param artworkImage the entity to save
     * @return the persisted entity
     */
    @Override
    public ArtworkImage save(ArtworkImage artworkImage) {
        log.debug("Request to save ArtworkImage : {}", artworkImage);
        ArtworkImage result = artworkImageRepository.save(artworkImage);
        return result;
    }

    /**
     *  Get all the artworkImages.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArtworkImage> findAll(Pageable pageable) {
        log.debug("Request to get all ArtworkImages");
        Page<ArtworkImage> result = artworkImageRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one artworkImage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ArtworkImage findOne(Long id) {
        log.debug("Request to get ArtworkImage : {}", id);
        ArtworkImage artworkImage = artworkImageRepository.findOne(id);
        return artworkImage;
    }

    /**
     *  Delete the  artworkImage by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArtworkImage : {}", id);
        artworkImageRepository.delete(id);
    }
}
