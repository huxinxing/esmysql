package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMCoinOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMCoinOperationRepository  extends JpaRepository<TMCoinOperation,Integer>, CrudRepository<TMCoinOperation, Integer> {
}
