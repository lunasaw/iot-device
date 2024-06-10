package io.github.lunasaw.iot.listener;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2024/6/8
 */
@Component
public class IotMqttActionListener implements IMqttActionListener {
    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        System.out.println("mqtt dynamic registration success");
        // TODO: 在此处参考一机一密进行连云和初始化
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        System.out.println("mqtt dynamic registration failed");
    }
}
