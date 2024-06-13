package io.github.lunasaw.iot.domain.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luna.common.constant.StrPoolConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/11
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "iot.product.device")
public class IotDeviceBO {

    public static final Map<String, IotSubDeviceBO> SUB_DEVICE_BO_MAP = new HashMap<>();
    private String                                  deviceSecret;
    private String                                  deviceName;
    private String                                  firmwareVersion   = IotDeviceConstant.Device.FIRMWARE_VERSION;
    private List<IotSubDeviceBO>                    iotSubDeviceBOS;

    public static void addSubDevice(List<IotSubDeviceBO> list) {
        for (IotSubDeviceBO iotSubDeviceBO : list) {
            SUB_DEVICE_BO_MAP.put(iotSubDeviceBO.getProductKey() + StrPoolConstant.UNDERLINE + iotSubDeviceBO.getDeviceName(), iotSubDeviceBO);
        }
    }
}
