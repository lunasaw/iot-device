package io.github.lunasaw.iot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.luna.common.text.StringTools;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.config.IotConfig;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.mqtt.MqttRequestService;
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

    @Autowired
    private MqttRequestService    mqttRequestService;

    @Test
    public void atest() {
        IotConfig.IotProduct product = iotConfig.getProduct();

        String topic = StringTools.format(IotDeviceConstant.TOPIC.POST_REPLY, ImmutableMap.of(IotDeviceConstant.Device.PRODUCT_KEY,
            product.getProductKey(), IotDeviceConstant.Device.DEVICE_NAME, product.getDevice().getDeviceName()));

        PublishMessageDTO publishMessageDTO = new PublishMessageDTO();
        publishMessageDTO.addReportThingsProperty("LightSwitch", 1);
        publishMessageDTO.addReportThingsProperty("LightCurrent", 7.8);

        resourceReportService.publish(publishMessageDTO);

        System.out.println(topic);
        mqttRequestService.subscribe(topic);
        while (true) {

        }
    }
}
