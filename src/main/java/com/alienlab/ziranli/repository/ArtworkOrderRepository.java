package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ArtworkOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtworkOrderRepository extends JpaRepository<ArtworkOrder,Long> {
    List<ArtworkOrder> findByUser(WechatUser wechatUser);
}
