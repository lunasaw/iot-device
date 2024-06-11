package io.github.lunasaw.iot.listener;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.dm.api.IShadowRRPC;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.dto.ShadowDTO;
import io.github.lunasaw.iot.handler.shadow.ShadowHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class IotShadowRrpcListener implements IShadowRRPC {

    @Autowired(required = false)
    private List<ShadowHandler> shadowHandlers;

    @Override
    public void onSubscribeSuccess(ARequest aRequest) {
        log.info("设备影子下行订阅成功 onSubscribeSuccess::aRequest = {}", JSON.toJSONString(aRequest));
    }

    @Override
    public void onSubscribeFailed(ARequest request, AError aError) {
        log.info("设备影子下行订阅失败 onSubscribeFailed::request = {}, aError = {}", JSON.toJSONString(request), JSON.toJSONString(aError));
    }

    @Override
    public void onReceived(ARequest request, AResponse response, IConnectRrpcHandle iConnectRrpcHandle) {
        log.info("收到设备影子下行指令 onReceived::request = {}, response = {}, ", JSON.toJSONString(request), JSON.toJSONString(response));

        if (response == null) {
            return;
        }

        if (CollectionUtils.isEmpty(shadowHandlers)) {
            return;
        }

        Object data = response.getData();
        ShadowDTO shadowDTO = new ShadowDTO(data);

        String result = StringUtils.EMPTY;
        for (ShadowHandler shadowHandler : shadowHandlers) {
            if (shadowHandler.isAccept(shadowDTO)) {
                result = shadowHandler.execute(shadowDTO);
            }
        }

        if (StringUtils.isNotBlank(result)) {
            response.data = result;
            iConnectRrpcHandle.onRrpcResponse(null, response);
        }
    }

    @Override
    public void onResponseSuccess(ARequest aRequest) {
        log.info("onResponseSuccess::aRequest = {}", aRequest);
    }

    @Override
    public void onResponseFailed(ARequest aRequest, AError aError) {
        log.info("onResponseFailed::aRequest = {}, aError = {}", aRequest, aError);
    }
}
