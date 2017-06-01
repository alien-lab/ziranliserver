package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.ArtworkOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ArtworkOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtworkOrderRepository extends JpaRepository<ArtworkOrder,Long> {

}
