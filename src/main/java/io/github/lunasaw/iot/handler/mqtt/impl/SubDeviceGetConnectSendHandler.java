package io.github.lunasaw.iot.handler.mqtt.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.domain.bo.IotDeviceBO;
import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import io.github.lunasaw.iot.domain.bo.ResponseBO;
import io.github.lunasaw.iot.handler.mqtt.AbstractConnectSendHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class SubDeviceGetConnectSendHandler extends AbstractConnectSendHandler {

    @Override
    public void onResponse(RequestSendDTO requestSendDTO) {
        ResponseBO<Object> boResponseBO = getBoResponseBO(requestSendDTO.getAResponse());
        if (boResponseBO == null) {
            return;
        }
        List<IotSubDeviceBO> data = (List<IotSubDeviceBO>)boResponseBO.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            return;
        }
        IotDeviceBO.addSubDevice(data);
    }

    @Override
    public boolean isAccept(RequestSendDTO requestSendDTO) {
        ResponseBO<Object> boResponseBO = getBoResponseBO(requestSendDTO.getAResponse());
        if (boResponseBO == null) {
            return false;
        }
        if (!IotDeviceConstant.Method.THING_TOPO_GET.equals(boResponseBO.getMethod())) {
            return false;
        }
        return true;
    }
}
