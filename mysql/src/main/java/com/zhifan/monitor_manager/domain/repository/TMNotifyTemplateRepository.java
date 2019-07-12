package com.zhifan.monitor_manager.domain.repository;

import com.zhifan.monitor_manager.domain.entity.TMNotifyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TMNotifyTemplateRepository  extends JpaRepository<TMNotifyTemplate,Integer>, CrudRepository<TMNotifyTemplate, Integer> {
}
