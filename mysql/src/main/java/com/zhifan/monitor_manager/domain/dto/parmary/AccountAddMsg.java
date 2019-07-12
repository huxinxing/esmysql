package com.zhifan.monitor_manager.domain.dto.parmary;

import lombok.Data;

@Data
public class AccountAddMsg {

    private Integer userId;

    private String name;

    private String password;

    private String email;

}
