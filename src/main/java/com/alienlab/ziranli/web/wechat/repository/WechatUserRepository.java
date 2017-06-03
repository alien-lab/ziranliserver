package com.alienlab.ziranli.web.wechat.repository;

import com.alienlab.ziranli.web.wechat.bean.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WechatUser entity.
 */
@Repository
public interface WechatUserRepository extends JpaRepository<WechatUser,Long> {
    WechatUser findWechatUserByOpenId(String openid);

}
