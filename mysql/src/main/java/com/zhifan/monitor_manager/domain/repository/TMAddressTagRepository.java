package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMAddressTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMAddressTagRepository  extends JpaRepository<TMAddressTag,Integer>, CrudRepository<TMAddressTag, Integer> {
}
