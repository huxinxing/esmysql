package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMUserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMUserAccountRepository extends JpaRepository<TMUserAccount,Integer>, CrudRepository<TMUserAccount, Integer> {
}
