package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.domain.ArtworkImage;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ArtworkImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtworkImageRepository extends JpaRepository<ArtworkImage,Long> {
    List<ArtworkImage> findArtworkImagesByArtwork(Artwork artwork);

}
