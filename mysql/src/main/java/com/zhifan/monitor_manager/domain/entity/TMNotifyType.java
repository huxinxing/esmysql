package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_notify_type")
public class TMNotifyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "notify_name")
    private Integer notifyName;

    @Column(name = "notify_code")
    private Integer notifyCode;


}
