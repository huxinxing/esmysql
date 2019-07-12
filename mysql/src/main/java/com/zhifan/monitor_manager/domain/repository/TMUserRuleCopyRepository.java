package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMUserRuleCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMUserRuleCopyRepository extends JpaRepository<TMUserRuleCopy,Integer>, CrudRepository<TMUserRuleCopy, Integer> {
}
