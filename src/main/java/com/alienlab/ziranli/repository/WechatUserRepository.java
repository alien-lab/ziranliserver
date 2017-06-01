package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.WechatUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WechatUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WechatUserRepository extends JpaRepository<WechatUser,Long> {

}
