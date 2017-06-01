package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.ExhibitionNameList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExhibitionNameList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExhibitionNameListRepository extends JpaRepository<ExhibitionNameList,Long> {

}
