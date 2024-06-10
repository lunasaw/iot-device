package io.github.lunasaw.iot.handler.mqtt;

import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.RequestSendDTO;

/**
 * @author luna
 * @date 2024/6/10
 */
public class AbstractMqttRequestSendHandler implements MqttRequestSendHandler {
    @Override
    public void onResponse(RequestSendDTO requestSendDTO) {

    }

    @Override
    public void onFailure(RequestSendDTO requestSendDTO) {

    }

    @Override
    public boolean isAccept(RequestSendDTO requestSendDTO) {
        return false;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(AError aError) {

    }
}
