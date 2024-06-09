package io.github.lunasaw.iot;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.handler.publish.LightPublishResourceHandler;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttInitParams;

import io.github.lunasaw.iot.config.AliyunIotConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest extends ApiTest {

    @Autowired
    private AliyunIotConfig aliyunIotConfig;

    @Autowired
    private LightPublishResourceHandler lightPublishResourceHandler;

    @Test
    public void atest() {

        String identity = "LightSwitch";
        ValueWrapper intWrapper = new ValueWrapper.IntValueWrapper(1);

        Map<String, ValueWrapper> reportData = new HashMap<String, ValueWrapper>();
        reportData.put(identity, intWrapper);

        PublishMessageDTO build = PublishMessageDTO.builder().reportData(reportData).build();
        lightPublishResourceHandler.publish(build);

        while (true) {

        }
    }
}
