package io.github.lunasaw.iot;

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

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest extends ApiTest {

    @Autowired
    private AliyunIotConfig aliyunIotConfig;

    @Test
    public void atest() {
        while (true) {

        }
    }
}
