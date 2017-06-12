package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.Artwork;
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
    List<ArtworkOrder> findArtworkOrdersByUserAndArtwork(WechatUser user, Artwork artwork);

    @Query("select a from ArtworkOrder a where a.artwork=?2 and a.user=?1 and a.payStatus='未支付'")
    List<ArtworkOrder> findArtworkOrdersByUserAndArtworkUnpay(WechatUser user, Artwork artwork);

    List<ArtworkOrder> findByUser(WechatUser wechatUser);
}
