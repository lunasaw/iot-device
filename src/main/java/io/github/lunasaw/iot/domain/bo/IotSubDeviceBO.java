package io.github.lunasaw.iot.domain.bo;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.aliyun.alink.dm.api.DeviceInfo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ConfigurationProperties(prefix = "iot.product.device.iot-sub-devices")
public class IotSubDeviceBO {

    private String productKey;
    private String deviceName;
    private String productSecret;
    private String deviceSecret;
    private String iotId;

    public DeviceInfo toDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.deviceName = this.deviceName;
        deviceInfo.productKey = this.productKey;
        deviceInfo.productSecret = this.productSecret;
        deviceInfo.deviceSecret = this.deviceSecret;
        return deviceInfo;
    }
}