package com.zhifan.monitor_manager.domain.dto.parmary;

import lombok.Data;

@Data
public class UserMonitorAddMsg {

    private Integer id;

    private String mobile;

    private String email;

    private String userName;

}
