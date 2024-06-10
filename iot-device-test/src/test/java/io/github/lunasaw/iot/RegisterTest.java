package io.github.lunasaw.iot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.lunasaw.iot.config.AliyunIotConfig;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.publish.AbstractPublishResourceReport;

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest extends ApiTest {

    @Autowired
    private AliyunIotConfig            aliyunIotConfig;

    @Autowired
    private AbstractPublishResourceReport publishResourceReport;

    @Test
    public void atest() {

        PublishMessageDTO publishMessageDTO = new PublishMessageDTO();
        publishMessageDTO.addReportThingsProperty("LightSwitch", 1);
        publishMessageDTO.addReportThingsProperty("LightCurrent", 7.8);


        publishResourceReport.publish(publishMessageDTO);

        while (true) {

        }
    }
}
