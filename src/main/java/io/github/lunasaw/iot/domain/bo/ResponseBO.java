package io.github.lunasaw.iot.domain.bo;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class ResponseBO<T> {

    @JSONField(name = "code")
    private int     code;

    @JSONField(name = "data")
    private T      data;

    @JSONField(name = "method")
    private String  method;

    @JSONField(name = "id")
    private String  id;

    @JSONField(name = "message")
    private String  message;

    @JSONField(name = "version")
    private String  version;
}