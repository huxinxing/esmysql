package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMCoinCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMCoinCategoryRepository  extends JpaRepository<TMCoinCategory,Integer>, CrudRepository<TMCoinCategory, Integer> {
}
