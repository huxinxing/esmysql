package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tb_statistical_large")
public class TBStatisticalLarge {

    public static Integer ISSEND_ZERO = 0; //充值

    public static Integer ISSEND_ONE = 1; //体现

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "deal_time")
    private Timestamp dealTime;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "turnover")
    private BigDecimal turnover;

    @Column(name = "coin_hash")
    private String coinHash;

    @Column(name = "trading_hash")
    private String tradingHash;

    @Column(name = "is_sender")
    private Integer IsSender;

}
