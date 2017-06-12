package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Artwork;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Artwork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtworkRepository extends JpaRepository<Artwork,Long> {
    @Query("select a from Artwork a order by a.id desc")
    List<Artwork> findAll();
}
