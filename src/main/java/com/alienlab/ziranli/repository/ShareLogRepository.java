package com.alienlab.ziranli.repository;

import com.alienlab.ziranli.domain.ShareLog;
import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ShareLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShareLogRepository extends JpaRepository<ShareLog,Long> {
    List<ShareLog> findShareLogsByUserAndShareTypeAndShareContentKey(WechatUser user,String shareType,String key);
}
