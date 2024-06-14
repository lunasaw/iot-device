package io.github.lunasaw.iot.handler.mqtt;

import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;

import io.github.lunasaw.iot.domain.RequestSendDTO;
import io.github.lunasaw.iot.domain.bo.ResponseBO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class AbstractConnectSendHandler implements ConnectSendHandler {
    public static @Nullable ResponseBO<Object> getBoResponseBO(AResponse response) {
        if (response == null) {
            return null;
        }
        Object data = response.getData();
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data.toString(), new TypeReference<ResponseBO<Object>>() {});
    }

    @Override
    public void onResponse(RequestSendDTO requestSendDTO) {
        log.info("onResponse::requestSendDTO = {}", JSON.toJSONString(requestSendDTO));
    }

    @Override
    public void onFailure(RequestSendDTO requestSendDTO) {
        log.error("onFailure::requestSendDTO = {}", requestSendDTO);
    }

    @Override
    public boolean isAccept(RequestSendDTO requestSendDTO) {
        return true;
    }

    public static void main(String[] args) {
        String data =
            "{\"code\":200,\"data\":[{\"deviceSecret\":\"664f57b42bffcfbf3f76e7393475263d\",\"productKey\":\"k1f4kl0qmEU\",\"deviceName\":\"luna_camera\"}],\"id\":\"2\",\"message\":\"success\",\"method\":\"thing.topo.get\",\"version\":\"1.0\"}";

        ResponseBO<Object> objectResponseBO = JSON.parseObject(data.toString(), new TypeReference<ResponseBO<Object>>() {});
        List<IotSubDeviceBO> list = (List<IotSubDeviceBO>)objectResponseBO.getData();
        System.out.println(JSON.toJSONString(list));
    }
}
