package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.WechatUserService;
import com.alienlab.ziranli.domain.WechatUser;
import com.alienlab.ziranli.repository.WechatUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing WechatUser.
 */
@Service
@Transactional
public class WechatUserServiceImpl implements WechatUserService{

    private final Logger log = LoggerFactory.getLogger(WechatUserServiceImpl.class);
    
    private final WechatUserRepository wechatUserRepository;

    public WechatUserServiceImpl(WechatUserRepository wechatUserRepository) {
        this.wechatUserRepository = wechatUserRepository;
    }

    /**
     * Save a wechatUser.
     *
     * @param wechatUser the entity to save
     * @return the persisted entity
     */
    @Override
    public WechatUser save(WechatUser wechatUser) {
        log.debug("Request to save WechatUser : {}", wechatUser);
        WechatUser result = wechatUserRepository.save(wechatUser);
        return result;
    }

    /**
     *  Get all the wechatUsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WechatUser> findAll(Pageable pageable) {
        log.debug("Request to get all WechatUsers");
        Page<WechatUser> result = wechatUserRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one wechatUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WechatUser findOne(Long id) {
        log.debug("Request to get WechatUser : {}", id);
        WechatUser wechatUser = wechatUserRepository.findOne(id);
        return wechatUser;
    }

    /**
     *  Delete the  wechatUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WechatUser : {}", id);
        wechatUserRepository.delete(id);
    }
}
