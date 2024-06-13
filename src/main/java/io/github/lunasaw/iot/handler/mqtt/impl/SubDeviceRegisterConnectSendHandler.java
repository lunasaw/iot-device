package io.github.lunasaw.iot.handler.mqtt.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.domain.bo.IotDeviceBO;
import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import io.github.lunasaw.iot.domain.bo.ResponseBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import io.github.lunasaw.iot.handler.mqtt.AbstractConnectSendHandler;

import java.util.List;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class SubDeviceRegisterConnectSendHandler extends AbstractConnectSendHandler {

    @Override
    public void onResponse(RequestSendDTO requestSendDTO) {
        log.info("SubDeviceRegisterConnectSendHandler onResponse::requestSendDTO = {}", JSON.toJSONString(requestSendDTO));

        ResponseBO<IotSubDeviceBO> boResponseBO = getBoResponseBO(requestSendDTO);
        if (boResponseBO == null) {
            return;
        }
        List<IotSubDeviceBO> data = boResponseBO.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            return;
        }
        IotDeviceBO.addSubDevice(data);
    }

    @Override
    public boolean isAccept(RequestSendDTO requestSendDTO) {
        ResponseBO<IotSubDeviceBO> boResponseBO = getBoResponseBO(requestSendDTO);
        if (boResponseBO == null) {
            return false;
        }
        if (!IotDeviceConstant.Method.THIND_SUB_REGISTER.equals(boResponseBO.getMethod())) {
            return false;
        }
        return true;
    }

    private static @Nullable ResponseBO<IotSubDeviceBO> getBoResponseBO(RequestSendDTO requestSendDTO) {
        if (requestSendDTO == null) {
            return null;
        }

        AResponse response = requestSendDTO.getAResponse();
        if (response == null) {
            return null;
        }
        Object data = response.getData();
        if (data == null) {
            return null;
        }
        ResponseBO<IotSubDeviceBO> boResponseBO = JSON.parseObject(JSON.toJSONString(data), new TypeReference<ResponseBO<IotSubDeviceBO>>() {});
        return boResponseBO;
    }
}
