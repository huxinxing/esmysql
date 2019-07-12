package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_coin_category")
public class TMCoinCategory {

    private static Integer NEEDMERGE_ZERO = 0;  //不需要合并

    private static Integer NEEDMERGE_ONE = 1;   //需要合并

    private static Integer CASESENSITIVE_ZERO = 0; //大小写不敏感

    private static Integer CASESENSITIVE_ONE = 1; //大小写敏感

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "modify_time")
    private Timestamp modifyTime;

    @Column(name = "coin_full_name")
    private String coinFullName;

    @Column(name = "coin_short_name")
    private String coinShortName;

    @Column(name = "coin_cn_name")
    private String coinCnName;

    @Column(name = "coin_code")
    private String coinCode;

    @Column(name = "coin_sort")
    private Integer coinSort;

    @Column(name = "coin_hash")
    private String coinHash;

    @Column(name = "parent_hash")
    private String parentHash;

    @Column(name = "need_merge")
    private Integer needMerge;

    @Column(name = "case_sensitive")
    private Integer caseSensitive;

}
