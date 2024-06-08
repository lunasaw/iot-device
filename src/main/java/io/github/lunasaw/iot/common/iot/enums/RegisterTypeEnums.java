package io.github.lunasaw.iot.common.iot.enums;

import lombok.Getter;

/**
 * case 1: 如果registerType里面填写了regnwl, 表明设备的一型一密方式为免预注册（即无需创建设备）
 * case 2: 如果这个字段为空, 或填写"register", 则表示为需要预注册的一型一密（需要实现创建设备）
 * 
 * @author luna
 */
@Getter
public enum RegisterTypeEnums {
    /**
     * 一型一密方式为免预注册（即无需创建设备）
     */
    REGNWL(0, "regnwl"),

    /**
     * 需要预注册的一型一密（需要实现创建设备）
     */
    REGISTER(1, "register");

    private int    code;
    private String value;

    RegisterTypeEnums(int i, String device) {

    }
}
