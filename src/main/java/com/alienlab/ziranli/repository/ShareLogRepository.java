package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.ShareLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ShareLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareLogRepository extends JpaRepository<ShareLog,Long> {

}
