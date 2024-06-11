package io.github.lunasaw.iot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/11
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "iot.device")
public class AliyunIotDevice {

    private String deviceSecret;
    private String deviceName;

}
