package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMUserRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface TMUserRuleRepository extends JpaRepository<TMUserRule,Integer>, CrudRepository<TMUserRule, Integer>, JpaSpecificationExecutor {
}
