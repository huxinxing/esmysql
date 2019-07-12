package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMNotifyHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TMNotifyHistoryRepository  extends JpaRepository<TMNotifyHistory,Integer>, CrudRepository<TMNotifyHistory, Integer>,JpaSpecificationExecutor {
    Page<TMNotifyHistory> findByUserId(Integer userId, PageRequest pageRequest);
}
