package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Exhibition;
import com.alienlab.ziranli.domain.ExhibitionArtwork;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ExhibitionArtwork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExhibitionArtworkRepository extends JpaRepository<ExhibitionArtwork, Long> {

    List<ExhibitionArtwork> findByExhibition(Exhibition exhibition);
}
