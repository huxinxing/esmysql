package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_notify_template")
public class TMNotifyTemplate {

    private static Integer NOTIFY_TYPE_ONE = 1;  //邮件通知

    private static Integer NOTIFY_TYPE_TWO = 2;  //短信通知

    private static Integer NOTIFY_TYPE_THREE = 3;   //邮件，短信通知

    private static Integer OPERATION_TYPE_ZERO = 0;  //转移（交易）

    private static Integer OPERATION_TYPE_ONE = 1;   //增发

    private static Integer STATUS_ZERO = 0;   //停用

    private static  Integer STATUS_ONE = 1;   //启用

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "notify_type")
    private Integer notifyType;

    @Column(name = "operation_type")
    private Integer operationType;

    @Column(name = "use_address")
    private Integer useAddress;

    @Column(name = "use_address_tag")
    private Integer useAddressTag;

    @Column(name = "subject")
    private String subject;

    @Column(name = "template")
    private String template;

    @Column(name = "status")
    private Integer status;

    @Column(name = "sms_template_id")
    private Integer smsTemplateId;

}
