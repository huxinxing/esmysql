package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMCoinUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMCoinUnitRepository  extends JpaRepository<TMCoinUnit,Integer>, CrudRepository<TMCoinUnit, Integer> {
}
