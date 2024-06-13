package io.github.lunasaw.iot.handler.mqtt;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import io.github.lunasaw.iot.domain.RequestSendDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class AbstractConnectSendHandler implements ConnectSendHandler {
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
}
