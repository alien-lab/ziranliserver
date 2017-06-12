package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.domain.Artwork;
import com.alienlab.ziranli.service.ArtworkOrderService;
import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.repository.ArtworkOrderRepository;
import com.alienlab.ziranli.service.ArtworkService;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import com.alienlab.ziranli.web.wechat.service.WechatService;
import com.alienlab.ziranli.web.wechat.service.WechatUserService;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ArtworkOrder.
 */
@Service
@Transactional
public class ArtworkOrderServiceImpl implements ArtworkOrderService{

    private final Logger log = LoggerFactory.getLogger(ArtworkOrderServiceImpl.class);

    private final ArtworkOrderRepository artworkOrderRepository;

    @Autowired
    WechatUserService wechatUserService;
    @Autowired
    ArtworkService artworkService;

    public ArtworkOrderServiceImpl(ArtworkOrderRepository artworkOrderRepository) {
        this.artworkOrderRepository = artworkOrderRepository;
    }

    /**
     * Save a artworkOrder.
     *
     * @param artworkOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public ArtworkOrder save(ArtworkOrder artworkOrder) {
        log.debug("Request to save ArtworkOrder : {}", artworkOrder);
        ArtworkOrder result = artworkOrderRepository.save(artworkOrder);
        return result;
    }

    /**
     *  Get all the artworkOrders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArtworkOrder> findAll(Pageable pageable) {
        log.debug("Request to get all ArtworkOrders");
        Page<ArtworkOrder> result = artworkOrderRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one artworkOrder by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ArtworkOrder findOne(Long id) {
        log.debug("Request to get ArtworkOrder : {}", id);
        ArtworkOrder artworkOrder = artworkOrderRepository.findOne(id);
        return artworkOrder;
    }

    /**
     *  Delete the  artworkOrder by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArtworkOrder : {}", id);
        artworkOrderRepository.delete(id);
    }

    @Override
    public List<ArtworkOrder> findUnpayOrder(String openid, Long artId) throws Exception {
        WechatUser user=wechatUserService.findUserByOpenid(openid);
        if(user==null){
            throw new Exception("未找到用户"+openid);
        }
        Artwork artwork=artworkService.findOne(artId);
        if(artwork==null){
            throw new Exception("未找到艺术品"+openid);
        }
        return artworkOrderRepository.findArtworkOrdersByUserAndArtworkUnpay(user,artwork);
    }

    @Override
    public List<ArtworkOrder> findUnpayOrder(WechatUser user, Artwork artwork) throws Exception {
        return artworkOrderRepository.findArtworkOrdersByUserAndArtworkUnpay(user,artwork);
    }

    @Override
    public List<ArtworkOrder> findMyArtworkOrder(WechatUser wechatUser) {
        log.debug("获取"+wechatUser+"艺术品购买记录");
        try{
            return artworkOrderRepository.findByUser(wechatUser);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
