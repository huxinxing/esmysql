package com.zhifan.monitor_manager.domain.dto.resultDto;

import lombok.Data;

@Data
public class ResponseResult {

    private String code;

    private String message;

    private Object data;

    public ResponseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
