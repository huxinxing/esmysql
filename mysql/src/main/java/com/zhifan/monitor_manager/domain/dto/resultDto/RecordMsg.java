package com.zhifan.monitor_manager.domain.dto.resultDto;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class RecordMsg {

    private BigInteger userId;

    private BigInteger ruleId;

    private String operationType = "大额交易监控";

    private Integer notifyType;

    private String target;

    private Integer notifyStatus;

    private String createTime;

    private String modifyTime;

    private String notifyContent;

}
