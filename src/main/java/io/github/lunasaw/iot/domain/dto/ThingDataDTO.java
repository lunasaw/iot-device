package io.github.lunasaw.iot.domain.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.common.iot.enums.ThingTypeEnums;
import lombok.Data;

/**
 * 物联网平台支持为产品定义多组功能（属性、服务和事件）。一组功能定义的集合，就是一个物模型模块。多个物模型模块，彼此互不影响。
 *
 * 物模型模块功能，解决了工业场景中复杂的设备建模，便于在同一产品下，开发不同功能的设备。
 *
 * 例如，电暖扇产品的功能属性有电源开关、档位（高、中、低）和室内温度，您可以在一个模块添加前2个属性，在另一个模块添加3个属性，
 * 然后分别在不同设备端，针对不同物模型模块功能进行开发。此时，该产品下不同设备就可以实现不同功能。
 * 物模型定义
 */
@Data
public class ThingDataDTO implements Serializable {
    /**
     * {@link ThingTypeEnums}
     */
    public String                    type;
    public String                    identifier;
    public Map<String, ValueWrapper> value = new HashMap<>();

    public synchronized void addValue(String key, ValueWrapper valueWrapper) {
        value.put(key, valueWrapper);
    }

    public synchronized void addValue(String data) {
        addValue(new ValueWrapper.StringValueWrapper(data));
    }

    public synchronized void addValue(ValueWrapper valueWrapper) {
        value.put(identifier, valueWrapper);
    }
}
