package io.github.lunasaw.iot.listener;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.handler.mqtt.ConnectSendHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 所有消息发送成功失败都是在这里处理
 * 
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class IotConnectSendListener implements IConnectSendListener {

    @Autowired(required = false)
    private List<ConnectSendHandler> connectSendHandlers;

    /**
     * 消息成功提交给操作系统的发送缓冲区。
     * 在网络波动等异常情况下，消息可能无法到达云端。
     * 如果上行的消息有对应的下行的reply, 建议通过reply报文来确认上行消息的到达。
     *
     * @param request
     * @param response
     */
    @Override
    public void onResponse(ARequest request, AResponse response) {
        if (CollectionUtils.isEmpty(connectSendHandlers)) {
            return;
        }
        RequestSendDTO requestSendDTO = RequestSendDTO.builder().aRequest(request).aResponse(response).build();
        for (ConnectSendHandler connectSendHandler : connectSendHandlers) {
            if (connectSendHandler.isAccept(requestSendDTO)) {
                connectSendHandler.onResponse(requestSendDTO);
            }
        }
    }

    @Override
    public void onFailure(ARequest request, AError aError) {
        if (CollectionUtils.isEmpty(connectSendHandlers)) {
            return;
        }
        RequestSendDTO requestSendDTO = RequestSendDTO.builder().aRequest(request).aError(aError).build();
        for (ConnectSendHandler connectSendHandler : connectSendHandlers) {
            if (connectSendHandler.isAccept(requestSendDTO)) {
                connectSendHandler.onResponse(requestSendDTO);
            }
        }
    }
}
