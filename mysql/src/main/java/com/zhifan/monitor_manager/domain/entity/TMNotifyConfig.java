package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_notify_config")
public class TMNotifyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private String value;

    @Column(name = "status")
    private Integer status;

    @Column(name = "category")
    private String category;

}
