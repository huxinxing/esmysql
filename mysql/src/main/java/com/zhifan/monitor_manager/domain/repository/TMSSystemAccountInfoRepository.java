package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMSystemAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TMSSystemAccountInfoRepository extends JpaRepository<TMSystemAccountInfo,Integer>, CrudRepository<TMSystemAccountInfo, Integer>, JpaSpecificationExecutor {

    TMSystemAccountInfo findByDisplayName(String name);

    TMSystemAccountInfo findByUserId(Integer userId);

    TMSystemAccountInfo findByEmail(String email);

}
