package io.github.lunasaw.iot.handler.mqtt;

import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.RequestSendDTO;

/**
 * mqtt订阅消息结果消息入口
 * 
 * @author luna
 * @date 2024/6/9
 */
public interface MqttScribeRequestHandler {

    /**
     * 该消息是否满足处理条件
     *
     * @param requestSendDTO
     * @return
     */
    boolean isAccept(RequestSendDTO requestSendDTO);

    // 以下是订阅 / 取消订阅处理 处理 ===================

    void onSuccess();

    void onFailure(AError aError);

}
