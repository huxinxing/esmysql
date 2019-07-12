package com.zhifan.monitor_manager.domain.dto.parmary;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class RuleAddMsg {

    private Integer id;

    private String ruleName;

    private BigInteger userId;

    private String coinHash;

    private Integer operationType;

    private String operationAddress;

    private BigDecimal thresholdValue;

    private String thresholdUnit;

    private Integer notifyType;

    private String addressTag;

}
