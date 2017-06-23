package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.domain.ArtworkOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Artwork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    @Query("select a from Artwork a order by a.id desc")
    List<Artwork> findAll();

    @Query("SELECT DISTINCT a.artworkType FROM Artwork a")
    List<String> findAllType();

    @Query("SELECT DISTINCT a.material FROM Artwork a")
    List<String> findAllMaterial();

    @Query("SELECT DISTINCT a.size FROM Artwork a")
    List<String> findAllSize();

    List<Artwork> findByArtworkType(String artworkType);

    List<Artwork> findBySize(String size);

    List<Artwork> findByMaterial(String material);

    List<Artwork> findByArtworkTypeAndMaterial(String artworkType, String material);

    List<Artwork> findByArtworkTypeAndSize(String artworkType, String size);

    List<Artwork> findByMaterialAndSize(String material, String size);

    List<Artwork> findByArtworkTypeAndMaterialAndSize(String artworkType, String material, String size);
}
