package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_coin_unit")
public class TMCoinUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "coin_hash")
    private String coinHash;

    @Column(name = "normal_unit")
    private String normalUnit;

    @Column(name = "min_unit")
    private String minUnit;

    @Column(name = "rate")
    private String rate;

}
