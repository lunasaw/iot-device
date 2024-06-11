package io.github.lunasaw.iot.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/11
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "iot.product")
public class AliyunIotProduct {
    private String                productKey;

    private String                productSecret;

    private List<AliyunIotDevice> deviceList;
}
