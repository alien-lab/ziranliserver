package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.domain.ArtworkImage;
import com.alienlab.ziranli.repository.ArtworkImageRepository;
import com.alienlab.ziranli.service.ArtworkService;
import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.repository.ArtworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Artwork.
 */
@Service
@Transactional
public class ArtworkServiceImpl implements ArtworkService {

    private final Logger log = LoggerFactory.getLogger(ArtworkServiceImpl.class);

    private final ArtworkRepository artworkRepository;

    @Autowired
    ArtworkImageRepository artworkImageRepository;

    public ArtworkServiceImpl(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    /**
     * Save a artwork.
     *
     * @param artwork the entity to save
     * @return the persisted entity
     */
    @Override
    public Artwork save(Artwork artwork) {
        log.debug("Request to save Artwork : {}", artwork);
        Artwork result = artworkRepository.save(artwork);
        return result;
    }

    /**
     * Get all the artworks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Artwork> findAll(Pageable pageable) {
        log.debug("Request to get all Artworks");
        Page<Artwork> result = artworkRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one artwork by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Artwork findOne(Long id) {
        log.debug("Request to get Artwork : {}", id);
        Artwork artwork = artworkRepository.findOne(id);
        return artwork;
    }

    /**
     * Delete the  artwork by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Artwork : {}", id);
        artworkRepository.delete(id);
    }

    @Override
    public List<ArtworkImage> loadImages(Long artwordId) throws Exception {
        Artwork artwork = artworkRepository.findOne(artwordId);
        if (artwork == null) {
            throw new Exception("未找到编码为" + artwordId + "的艺术品");
        }

        return artworkImageRepository.findArtworkImagesByArtwork(artwork);
    }

    @Override
    public List<Artwork> getAll() {
        return artworkRepository.findAll();
    }

    @Override
    public List<String> getAllType() {
        return artworkRepository.findAllType();
    }

    @Override
    public List<String> getAllMaterial() {
        return artworkRepository.findAllMaterial();
    }

    @Override
    public List<String> getAllSize() {
        return artworkRepository.findAllSize();
    }

    @Override
    public List<Artwork> getByType(String type) {
        return artworkRepository.findByArtworkType(type);
    }

    @Override
    public List<Artwork> getBySize(String size) {
        return artworkRepository.findBySize(size);
    }

    @Override
    public List<Artwork> getByMaterial(String material) {
        return artworkRepository.findByMaterial(material);
    }

    @Override
    public List<Artwork> getByTypeAndMaterial(String type, String material) {
        return artworkRepository.findByArtworkTypeAndMaterial(type, material);
    }

    @Override
    public List<Artwork> getByTypeAndSize(String type, String size) {
        return artworkRepository.findByArtworkTypeAndSize(type, size);
    }

    @Override
    public List<Artwork> getByMaterialAndSize(String material, String size) {
        return artworkRepository.findByMaterialAndSize(material, size);
    }

    @Override
    public List<Artwork> getByTypeAndMaterialAndSize(String type, String material, String size) {
        return artworkRepository.findByArtworkTypeAndMaterialAndSize(type, material, size);
    }

}
