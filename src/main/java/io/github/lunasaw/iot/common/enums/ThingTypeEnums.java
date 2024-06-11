
package io.github.lunasaw.iot.common.enums;

import lombok.Getter;

/**
 * 属性（Property） 用于描述设备运行时具体信息和状态。
 * 例如，环境监测设备所读取的当前环境温度、智能灯开关状态、电风扇风力等级等。
 *
 * 属性可分为读写和只读两种类型。读写类型支持读取和设置属性值，只读类型仅支持读取属性值。
 *
 * 服务（Service） 指设备可供外部调用的指令或方法。服务调用中可设置输入和输出参数。输入参数是服务执行时的参数，输出参数是服务执行后的结果。
 * 相比于属性，服务可通过一条指令实现更复杂的业务逻辑，例如执行某项特定的任务。
 *
 * 服务分为异步和同步两种调用方式。
 *
 * 事件（Event） 设备运行时，主动上报给云端的信息，一般包含需要被外部感知和处理的信息、告警和故障。事件中可包含多个输出参数。
 * 例如，某项任务完成后的通知信息；设备发生故障时的温度、时间信息；设备告警时的运行状态等。
 *
 * 事件可以被订阅和推送。
 * 
 * @author luna
 */
@Getter
public enum ThingTypeEnums {
    /**
     * 属性（Property） 用于描述设备运行时具体信息和状态。
     */
    PROPERTY(0, "Property"),

    /**
     * 服务（Service） 指设备可供外部调用的指令或方法。服务调用中可设置输入和输出参数。输入参数是服务执行时的参数，输出参数是服务执行后的结果。
     */
    SERVICE(1, "Service"),

    /**
     * 事件（Event） 设备运行时，主动上报给云端的信息，一般包含需要被外部感知和处理的信息、告警和故障。事件中可包含多个输出参数。
     */
    EVENT(2, "Event");

    private int    code;
    private String value;

    ThingTypeEnums(int i, String value) {
        this.code = i;
        this.value = value;
    }
}
