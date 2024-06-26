package io.github.lunasaw.iot.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.lunasaw.iot.domain.bo.IotDeviceBO;
import org.springframework.boot.context.properties.ConfigurationProperties;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/7
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "iot")
public class IotConfig {

    private IotProduct product;

    /**
     * 实例 ID。您可在物联网平台控制台的实例概览页面，查看当前实例的 ID。
     */
    private String     iotInstanceId;

    private String     mqttHostUrl;

    private String     region;

    /**
     * @author luna
     * @date 2024/6/11
     */
    @Slf4j
    @Data
    @ConfigurationProperties(prefix = "iot.product")
    public static class IotProduct {
        private String    productKey;

        private String    productSecret;

        private IotDeviceBO device;

    }


}
