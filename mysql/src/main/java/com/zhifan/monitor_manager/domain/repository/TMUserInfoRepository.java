package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TMUserInfoRepository extends JpaRepository<TMUserInfo,Integer>,CrudRepository<TMUserInfo, Integer>,JpaSpecificationExecutor {
}
