package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_user_rule")
public class TMUserRule {

    private static Integer OPERATION_TYPE_ZERO = 0;  //转移（交易）

    private static Integer OPERATION_TYPE_ONE = 1;   //增发

    private static Integer NOTIFY_TYPE_ONE = 1;  //邮件通知

    private static Integer NOTIFY_TYPE_TWO = 2;  //短信通知

    private static Integer NOTIFY_TYPE_THREE = 3;   //邮件，短信通知

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "coin_hash")
    private String coinHash;

    @Column(name = "operation_type")
    private Integer operationType;

    @Column(name = "operation_address")
    private String operationAddress;

    @Column(name = "threshold_value")
    private BigDecimal thresholdValue;

    @Column(name = "threshold_unit")
    private String thresholdUnit;

    @Column(name = "notify_type")
    private Integer notifyType;

    @Column(name = "address_tag")
    private String addressTag;



}
