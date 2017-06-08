package com.alienlab.ziranli.service;

import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ArtworkOrder.
 */
public interface ArtworkOrderService {

    /**
     * Save a artworkOrder.
     *
     * @param artworkOrder the entity to save
     * @return the persisted entity
     */
    ArtworkOrder save(ArtworkOrder artworkOrder);

    /**
     *  Get all the artworkOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ArtworkOrder> findAll(Pageable pageable);

    /**
     *  Get the "id" artworkOrder.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ArtworkOrder findOne(Long id);

    /**
     *  Delete the "id" artworkOrder.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    //查询个人艺术品购买记录
    List<ArtworkOrder> findMyArtworkOrder(WechatUser wechatUser);
}
