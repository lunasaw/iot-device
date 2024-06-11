package io.github.lunasaw.iot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.lunasaw.iot.config.IotConfig;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.publish.ResourceReportService;

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest extends ApiTest {

    @Autowired
    private IotConfig             iotConfig;

    @Autowired
    private ResourceReportService resourceReportService;

    @Test
    public void atest() {

        IotConfig.IotProduct product = iotConfig.getProduct();
        String deviceKey = product.getProductKey() + "_" +
            product.getDevice().getDeviceName();
        PublishMessageDTO publishMessageDTO = new PublishMessageDTO();
        publishMessageDTO.addReportThingsProperty("LightSwitch", 1);
        publishMessageDTO.addReportThingsProperty("LightCurrent", 7.8);

        resourceReportService.publish(publishMessageDTO);

        while (true) {

        }
    }
}
