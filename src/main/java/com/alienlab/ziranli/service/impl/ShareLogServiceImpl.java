package com.alienlab.ziranli.service.impl;

import com.alienlab.ziranli.service.ShareLogService;
import com.alienlab.ziranli.domain.ShareLog;
import com.alienlab.ziranli.repository.ShareLogRepository;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import com.alienlab.ziranli.web.wechat.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing ShareLog.
 */
@Service
@Transactional
public class ShareLogServiceImpl implements ShareLogService{

    private final Logger log = LoggerFactory.getLogger(ShareLogServiceImpl.class);

    private final ShareLogRepository shareLogRepository;

    @Autowired
    WechatUserService wechatUserService;

    public ShareLogServiceImpl(ShareLogRepository shareLogRepository) {
        this.shareLogRepository = shareLogRepository;
    }

    /**
     * Save a shareLog.
     *
     * @param shareLog the entity to save
     * @return the persisted entity
     */
    @Override
    public ShareLog save(ShareLog shareLog) {
        log.debug("Request to save ShareLog : {}", shareLog);
        ShareLog result = shareLogRepository.save(shareLog);
        return result;
    }

    /**
     *  Get all the shareLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShareLog> findAll(Pageable pageable) {
        log.debug("Request to get all ShareLogs");
        Page<ShareLog> result = shareLogRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one shareLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ShareLog findOne(Long id) {
        log.debug("Request to get ShareLog : {}", id);
        ShareLog shareLog = shareLogRepository.findOne(id);
        return shareLog;
    }

    /**
     *  Delete the  shareLog by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShareLog : {}", id);
        shareLogRepository.delete(id);
    }

    @Override
    public List<ShareLog> getCourseShareLog(Long courseId, String openid) throws Exception {
        WechatUser user=wechatUserService.findUserByOpenid(openid);
        if(user==null){
            throw new Exception("未找到用户"+openid);
        }
        List<ShareLog> result=shareLogRepository.findShareLogsByUserAndShareTypeAndShareContentKey(user,"course",String.valueOf(courseId));

        return result;
    }


}
