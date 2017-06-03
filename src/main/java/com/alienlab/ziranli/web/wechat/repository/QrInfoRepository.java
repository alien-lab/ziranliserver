package com.alienlab.ziranli.web.wechat.repository;

import com.alienlab.ziranli.web.wechat.bean.entity.QrInfo;
import com.alienlab.ziranli.web.wechat.bean.entity.dto.QrInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the QrInfo entity.
 */
@Repository
public interface QrInfoRepository extends JpaRepository<QrInfo,Long> {
    @Query(value = " from QrType a")
     List<QrInfoDto> findByQrTypeId();
}
