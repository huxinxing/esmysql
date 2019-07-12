package com.zhifan.monitor_manager.domain.dto.resultDto;

import lombok.Data;

@Data
public class UserMonitorMsg {

    private Integer id;

    private String createTime;

    private String modifyTime;

    private String mobile;

    private String email;

    private Integer isEnable;

    private String userName;

}
