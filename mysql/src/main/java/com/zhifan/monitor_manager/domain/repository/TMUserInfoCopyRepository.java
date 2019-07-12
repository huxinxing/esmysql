package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMUserInfoCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMUserInfoCopyRepository extends JpaRepository<TMUserInfoCopy,Integer> , CrudRepository<TMUserInfoCopy, Integer> {
}
