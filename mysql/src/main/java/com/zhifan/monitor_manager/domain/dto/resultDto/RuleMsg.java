package com.zhifan.monitor_manager.domain.dto.resultDto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class RuleMsg {

    private Integer id;

    private BigInteger userId;

    private Integer operationType;

    private String thresholdUnit;

    private String operationAddress;

    private String addressTag;

    private BigDecimal thresholdValue;

    private String coinHash;

    private String ruleName;

    private Integer notifyType;

    private String createTime;

    private String modifyTime;

}
