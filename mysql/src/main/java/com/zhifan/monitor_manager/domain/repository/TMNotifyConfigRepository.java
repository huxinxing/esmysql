package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMNotifyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMNotifyConfigRepository  extends JpaRepository<TMNotifyConfig,Integer>, CrudRepository<TMNotifyConfig, Integer> {
}
