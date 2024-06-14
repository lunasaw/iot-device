package io.github.lunasaw.iot.handler.resource;

import io.github.lunasaw.iot.domain.dto.ResourceRequestDTO;

/**
 * 注册资源 接口示例
 * 1.先订阅下行的 topic
 * 2.云端通过该 topic 下行，发送指令；
 * 3.收到数据并相应。
 * 
 * @author luna
 * @date 2024/6/10
 */
public interface ResourceHandler {

    /**
     * 消息处理
     *
     * @param requestDTO
     */
    String execute(ResourceRequestDTO requestDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param requestDTO
     * @return
     */
    boolean isAccept(ResourceRequestDTO requestDTO);
}
