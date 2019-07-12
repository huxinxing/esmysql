package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_user_info")
public class TMUserInfo {

    public static Integer IS_ENABLE_ZERO = 0;  //停用

    public static Integer IS_ENABLE_ONE = 1;   //启用

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "is_enable")
    private Integer isEnable;

    @Column(name = "user_name")
    private String userName;

}
