package com.alienlab.ziranli.web.wechat.repository;

import com.alienlab.ziranli.web.wechat.bean.entity.WechatMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WechatMessageLog entity.
 */
@Repository
public interface WechatMessageLogRepository extends JpaRepository<WechatMessageLog,Long> {

}
