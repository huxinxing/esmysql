package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user_account_info")
public class TMSystemAccountInfo {


    public static Integer STATUS_ONE = 1; //可用

    public static  Integer STATUS_ZERO = 0;  //停用

    public static  Integer LOGIN_ZERO = 0;  //用户未登录

    public static  Integer LOGIN_ONE = 1; //用户已登录

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email")
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "security_password")
    private String securityPassword;

    @Column(name = "salt")
    private String salt;

    @Column(name = "statu")
    private Integer statu;

    @Column(name = "is_login")
    private Integer isLogin;

    @Column(name = "google_code")
    private String googleCode;

    @Column(name = "modify_name")
    private Timestamp modifyName;

    @Column(name = "create_time")
    private Timestamp createTime;

    public boolean isBindGoogleCode(){
        if (StringUtils.isEmpty(googleCode)){
            return true;
        }else {
            return false;
        }
    }

}
