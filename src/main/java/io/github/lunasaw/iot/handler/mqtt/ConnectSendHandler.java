package io.github.lunasaw.iot.handler.mqtt;

import io.github.lunasaw.iot.domain.RequestSendDTO;

/**
 * 发送消息成功失败处理入口
 * 
 * @author luna
 * @date 2024/6/9
 */
public interface ConnectSendHandler {

    // 普通消息发送处理

    /**
     * 成功处理
     *
     * @param requestSendDTO
     * @return
     */
    void onResponse(RequestSendDTO requestSendDTO);

    /**
     * 失败处理
     * 
     * @param requestSendDTO
     */
    void onFailure(RequestSendDTO requestSendDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param requestSendDTO
     * @return
     */
    boolean isAccept(RequestSendDTO requestSendDTO);
}
