package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.domain.ArtworkImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Artwork.
 */
public interface ArtworkService {

    /**
     * Save a artwork.
     *
     * @param artwork the entity to save
     * @return the persisted entity
     */
    Artwork save(Artwork artwork);

    /**
     * Get all the artworks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Artwork> findAll(Pageable pageable);

    /**
     * Get the "id" artwork.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Artwork findOne(Long id);

    /**
     * Delete the "id" artwork.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<ArtworkImage> loadImages(Long artwordId) throws Exception;

    List<Artwork> getAll();

    List<String> getAllType();

    List<String> getAllMaterial();

    List<String> getAllSize();

    List<Artwork> getByType(String type);

    List<Artwork> getBySize(String size);

    List<Artwork> getByMaterial(String material);

    List<Artwork> getByTypeAndMaterial(String type, String material);

    List<Artwork> getByTypeAndSize(String type, String size);

    List<Artwork> getByMaterialAndSize(String material, String size);

    List<Artwork> getByTypeAndMaterialAndSize(String type, String material, String size);
}
