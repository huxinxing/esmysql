package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_coin_operation")
public class TMCoinOperation {

    public static Integer ISSEND_ZERO = 0; //未发送

    public static Integer ISSEND_ONE = 1; //已发送

    private static Integer OPERATION_TYPE_ZERO = 0;  //转移（交易）

    private static Integer OPERATION_TYPE_ONE = 1;   //增发

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "operation_time")
    private Timestamp operationTime;

    @Column(name = "operation_type")
    private Integer operationType;

    @Column(name = "operation_value")
    private BigDecimal operationValue;

    @Column(name = "operation_address")
    private String operationAddress;

    @Column(name = "operation_hash")
    private String operationHash;

    @Column(name = "is_sender")
    private Integer isSender;

    @Column(name = "coin_hash")
    private String coinHash;

    @Column(name = "block_height")
    private BigInteger blockHeight;


}
