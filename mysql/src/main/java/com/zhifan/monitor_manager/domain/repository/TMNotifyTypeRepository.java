package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMNotifyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMNotifyTypeRepository  extends JpaRepository<TMNotifyType,Integer>, CrudRepository<TMNotifyType, Integer> {
}
