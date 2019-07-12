package com.zhifan.monitor_manager.domain.dto.resultDto;

import lombok.Data;

@Data
public class AccountMsg {

    private Integer userId;

    private String name;

    private String email;

    private Integer status;   //0表示停用 1表示启用

}
