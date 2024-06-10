package io.github.lunasaw.iot.listener;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.handler.mqtt.MqttRequestSendHandler;

/**
 *
 * IConnectSubscribeListener 订阅成功后，相应的下行消息，会通过认证与连接的连接状态与下行消息监听中的IConnectNotifyListener对象中透出。
 * 如果设备不再需要订阅指定Topic，需要主动取消订阅。否则订阅关系一直存在，您将持续收到订阅Topic发送的消息。
 * 
 * @author luna
 * @date 2024/6/10
 */
@Component
public class IotMqttSendAndSubscribeListener implements IConnectSendListener, IConnectSubscribeListener, IConnectUnscribeListener {

    @Autowired(required = false)
    private List<MqttRequestSendHandler> mqttRequestSendHandlers;

    /**
     * 消息成功提交给操作系统的发送缓冲区。
     * 在网络波动等异常情况下，消息可能无法到达云端。
     * 如果上行的消息有对应的下行的reply, 建议通过reply报文来确认上行消息的到达。
     * 
     * @param aRequest
     * @param aResponse
     */
    @Override
    public void onResponse(ARequest aRequest, AResponse aResponse) {
        if (CollectionUtils.isEmpty(mqttRequestSendHandlers)) {
            return;
        }
        RequestSendDTO requestSendDTO = RequestSendDTO.builder().aRequest(aRequest).aResponse(aResponse).build();
        for (MqttRequestSendHandler mqttRequestSendHandler : mqttRequestSendHandlers) {
            if (mqttRequestSendHandler.isAccept(requestSendDTO)) {
                mqttRequestSendHandler.onResponse(requestSendDTO);
            }
        }
    }

    @Override
    public void onFailure(ARequest aRequest, AError aError) {
        if (CollectionUtils.isEmpty(mqttRequestSendHandlers)) {
            return;
        }
        RequestSendDTO requestSendDTO = RequestSendDTO.builder().aRequest(aRequest).aError(aError).build();
        for (MqttRequestSendHandler mqttRequestSendHandler : mqttRequestSendHandlers) {
            if (mqttRequestSendHandler.isAccept(requestSendDTO)) {
                mqttRequestSendHandler.onResponse(requestSendDTO);
            }
        }
    }

    @Override
    public void onSuccess() {
        if (CollectionUtils.isEmpty(mqttRequestSendHandlers)) {
            return;
        }
        for (MqttRequestSendHandler mqttRequestSendHandler : mqttRequestSendHandlers) {
            mqttRequestSendHandler.onSuccess();
        }
    }

    @Override
    public void onFailure(AError aError) {
        if (CollectionUtils.isEmpty(mqttRequestSendHandlers)) {
            return;
        }
        RequestSendDTO requestSendDTO = RequestSendDTO.builder().aError(aError).build();
        for (MqttRequestSendHandler mqttRequestSendHandler : mqttRequestSendHandlers) {
            if (mqttRequestSendHandler.isAccept(requestSendDTO)) {
                mqttRequestSendHandler.onFailure(requestSendDTO.getAError());
            }
        }
    }
}
