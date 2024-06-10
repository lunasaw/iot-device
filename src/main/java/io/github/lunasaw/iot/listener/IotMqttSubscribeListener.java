package io.github.lunasaw.iot.listener;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.handler.mqtt.MqttScribeRequestHandler;

/**
 *
 * IConnectSubscribeListener 订阅成功后，相应的下行消息，会通过认证与连接的连接状态与下行消息监听中的IConnectNotifyListener对象中透出。
 * 如果设备不再需要订阅指定Topic，需要主动取消订阅。否则订阅关系一直存在，您将持续收到订阅Topic发送的消息。
 * 
 * @author luna
 * @date 2024/6/10
 */
@Component
public class IotMqttSubscribeListener implements IConnectSubscribeListener, IConnectUnscribeListener {

    @Autowired(required = false)
    private List<MqttScribeRequestHandler> mqttScribeRequestHandlers;

    @Override
    public void onSuccess() {
        if (CollectionUtils.isEmpty(mqttScribeRequestHandlers)) {
            return;
        }
        for (MqttScribeRequestHandler mqttScribeRequestHandler : mqttScribeRequestHandlers) {
            mqttScribeRequestHandler.onSuccess();
        }
    }

    @Override
    public void onFailure(AError aError) {
        if (CollectionUtils.isEmpty(mqttScribeRequestHandlers)) {
            return;
        }
        RequestSendDTO requestSendDTO = RequestSendDTO.builder().aError(aError).build();
        for (MqttScribeRequestHandler mqttScribeRequestHandler : mqttScribeRequestHandlers) {
            if (mqttScribeRequestHandler.isAccept(requestSendDTO)) {
                mqttScribeRequestHandler.onFailure(requestSendDTO.getAError());
            }
        }
    }
}
