package com.zhifan.monitor_manager.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tm_address_tag")
public class TMAddressTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "coin_hash")
    private String coinHash;

    @Column(name = "address_hash")
    private String addressHash;

    @Column(name = "adderss_tag")
    private String AddressTag;


}
