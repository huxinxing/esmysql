package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_notify_history")
public class TMNotifyHistory {


    private static Integer NOTIFY_TYPE_ONE = 1;  //邮件通知

    private static Integer NOTIFY_TYPE_TWO = 2;  //短信通知

    private static Integer NOTIFY_TYPE_THREE = 3;   //邮件，短信通知

    private static Integer NOTIFY_STATUS_ZERO = 0;  //未发送

    private static Integer NOTIFY_STATUS_ONE = 1;  //发送成功

    private static Integer NOTIFY_STATUS_TWO = 2;  //发送失败

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

    @Column(name = "rule_id")
    private BigInteger ruleId;

    @Column(name = "notify_type")
    private Integer notifyType;

    @Column(name = "target")
    private String target;

    @Column(name = "notify_content")
    private String notifyContent;

    @Column(name = "notify_status")
    private Integer notifyStatus;


}
