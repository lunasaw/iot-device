package io.github.lunasaw.iot.handler.mqtt.impl;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;


import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.domain.bo.IotDeviceBO;
import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import io.github.lunasaw.iot.domain.bo.ResponseBO;
import io.github.lunasaw.iot.handler.mqtt.AbstractConnectSendHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * RegisterConnect
 * 
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class SubDeviceRegisterConnectSendHandler extends AbstractConnectSendHandler {

    public static void main(String[] args) {
        String data =
            "{\"code\":200,\"data\":[{\"deviceSecret\":\"664f57b42bffcfbf3f76e7393475263d\",\"productKey\":\"k1f4kl0qmEU\",\"deviceName\":\"luna_camera\"}],\"id\":\"2\",\"message\":\"success\",\"method\":\"thing.topo.get\",\"version\":\"1.0\"}";

        ResponseBO boResponseBO = JSON.parseObject(data, new TypeReference<ResponseBO<List<IotSubDeviceBO>>>() {});
        System.out.println(JSON.toJSONString(boResponseBO));
    }

    @Override
    public void onResponse(RequestSendDTO requestSendDTO) {
        log.info("SubDeviceRegisterConnectSendHandler onResponse::requestSendDTO = {}", JSON.toJSONString(requestSendDTO));

        ResponseBO<Object> boResponseBO = getBoResponseBO(requestSendDTO.getAResponse());
        if (boResponseBO == null) {
            return;
        }
        List<IotSubDeviceBO> data = JSON.parseObject(JSON.toJSONString(boResponseBO.getData()), new TypeReference<List<IotSubDeviceBO>>() {});
        if (CollectionUtils.isEmpty(data)) {
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
        if (!IotDeviceConstant.Method.THING_SUB_REGISTER.equals(boResponseBO.getMethod())) {
            return false;
        }
        return true;
    }
}
