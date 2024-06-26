package io.github.lunasaw.iot.listener;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.linkkit.api.LinkKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.dto.CotaDTO;
import io.github.lunasaw.iot.handler.cota.CotaHandler;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class IotConnectRrpcListener implements IConnectRrpcListener, InitializingBean {

    @Autowired(required = false)
    private List<CotaHandler> cotaHandlers;

    @Override
    public void onSubscribeSuccess(ARequest request) {
        log.info("onSubscribeSuccess::request = {}", JSON.toJSONString(request));
        if (request instanceof MqttRrpcRequest) {
            // 云端下行数据 拿到
            if (CollectionUtils.isEmpty(cotaHandlers)) {
                return;
            }
            CotaDTO cotaDTO = new CotaDTO(((MqttRrpcRequest)request).payloadObj);
            for (CotaHandler cotaHandler : cotaHandlers) {
                if (cotaHandler.isAccept(cotaDTO)) {
                    cotaHandler.execute(cotaDTO);
                }
            }
        }
    }

    @Override
    public void onSubscribeFailed(ARequest aRequest, AError aError) {
        log.info("onSubscribeFailed::aRequest = {}, aError = {}", JSON.toJSONString(aRequest), JSON.toJSONString(aError));
    }

    @Override
    public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
        log.info("onReceived::aRequest = {}", JSON.toJSONString(aRequest));
        iConnectRrpcHandle.onRrpcResponse(null, null);
    }

    @Override
    public void onResponseSuccess(ARequest aRequest) {
        log.info("onResponseSuccess::aRequest = {}", JSON.toJSONString(aRequest));
    }

    @Override
    public void onResponseFailed(ARequest aRequest, AError aError) {
        log.info("onResponseFailed::aRequest = {}, aError = {}", JSON.toJSONString(aRequest), JSON.toJSONString(aError));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LinkKit.getInstance().getDeviceCOTA().setCOTAChangeListener(this);
    }
}
