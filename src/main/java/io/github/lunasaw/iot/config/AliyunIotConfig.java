package io.github.lunasaw.iot.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/7
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "iot")
public class AliyunIotConfig {

    private List<AliyunIotProduct> productList;

    /**
     * 实例 ID。您可在物联网平台控制台的实例概览页面，查看当前实例的 ID。
     */
    private String                 iotInstanceId;

    private String                 mqttHostUrl;

    private String                 region;
}
