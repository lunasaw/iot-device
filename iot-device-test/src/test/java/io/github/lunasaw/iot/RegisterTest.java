package io.github.lunasaw.iot;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.config.AliyunIotConfig;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.publish.BasicPublishResourceReport;

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest extends ApiTest {

    @Autowired
    private AliyunIotConfig            aliyunIotConfig;

    @Autowired
    private BasicPublishResourceReport publishResourceReport;

    @Test
    public void atest() {

        String identity = "LightSwitch";
        ValueWrapper intWrapper = new ValueWrapper.IntValueWrapper(1);

        Map<String, ValueWrapper> reportData = new HashMap<String, ValueWrapper>();
        reportData.put(identity, intWrapper);

        String lightCurrent = "LightCurrent";
        ValueWrapper lightCurrentValue = new ValueWrapper.DoubleValueWrapper(7.8);
        reportData.put(lightCurrent, lightCurrentValue);

        PublishMessageDTO build = PublishMessageDTO.builder().reportData(reportData).build();
        publishResourceReport.publish(build);

        while (true) {

        }
    }
}
