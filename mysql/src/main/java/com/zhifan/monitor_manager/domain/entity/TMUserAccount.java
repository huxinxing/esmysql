package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_user_account")
public class TMUserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "sms_count")
    private Integer smsCount;

    @Column(name = "email_count")
    private Integer emailCount;

    @Column(name = "points")
    private Integer points;

    @Column(name = "bonus")
    private Integer bonus;

}
