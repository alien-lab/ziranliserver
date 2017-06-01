package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.ExhibitionArtwork;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExhibitionArtwork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExhibitionArtworkRepository extends JpaRepository<ExhibitionArtwork,Long> {

}
