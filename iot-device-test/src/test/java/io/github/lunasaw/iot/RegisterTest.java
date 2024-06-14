package io.github.lunasaw.iot;

import com.aliyun.alink.dm.api.DeviceInfo;
import io.github.lunasaw.iot.domain.bo.IotDeviceBO;
import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableMap;
import com.luna.common.text.StringTools;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.config.IotConfig;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.gateway.GatewayService;
import io.github.lunasaw.iot.mqtt.MqttRequestService;
import io.github.lunasaw.iot.publish.ResourceReportService;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private GatewayService        gatewayService;

    @SneakyThrows
    @Test
    public void btest() {
        List<IotSubDeviceBO> iotSubDeviceBOS = iotConfig.getProduct().getDevice().getIotSubDevices();
        gatewayService.gatewaySubDeviceRegister(iotSubDeviceBOS);
        gatewayService.gatewayGetSubDevices();


    }

    @Test
    public void atest() {
        IotConfig.IotProduct product = iotConfig.getProduct();

        String topic = StringTools.format(IotDeviceConstant.Topic.POST_REPLY, ImmutableMap.of(IotDeviceConstant.Device.PRODUCT_KEY,
            product.getProductKey(), IotDeviceConstant.Device.DEVICE_NAME, product.getDevice().getDeviceName()));

        PublishMessageDTO publishMessageDTO = new PublishMessageDTO();
        publishMessageDTO.addReportThingsProperty("disk_usage", 88);
        publishMessageDTO.addReportThingsProperty("cpu_core_number", 4);

        resourceReportService.publish(publishMessageDTO);

        System.out.println(topic);
        mqttRequestService.subscribe(topic);

    }

    @After
    public void method() {
        while (true) {

        }
    }
}
