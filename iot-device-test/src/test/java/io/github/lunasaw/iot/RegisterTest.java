package io.github.lunasaw.iot;

import io.github.lunasaw.iot.config.AliyunIotProduct;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.lunasaw.iot.config.AliyunIotConfig;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.publish.ResourceReportService;

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest extends ApiTest {

    @Autowired
    private AliyunIotConfig       aliyunIotConfig;

    @Autowired
    private ResourceReportService resourceReportService;

    @Test
    public void atest() {

        AliyunIotProduct aliyunIotProduct = aliyunIotConfig.getProductList().get(0);
        String deviceKey = aliyunIotProduct.getProductKey() + "_" + aliyunIotProduct.getDeviceList().get(0).getDeviceName();
        PublishMessageDTO publishMessageDTO = new PublishMessageDTO(deviceKey);
        publishMessageDTO.addReportThingsProperty("LightSwitch", 1);
        publishMessageDTO.addReportThingsProperty("LightCurrent", 7.8);

        resourceReportService.publish(publishMessageDTO);

        while (true) {

        }
    }
}
