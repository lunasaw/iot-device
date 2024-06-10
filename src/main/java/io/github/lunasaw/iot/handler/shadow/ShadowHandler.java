package io.github.lunasaw.iot.handler.shadow;

import io.github.lunasaw.iot.domain.dto.ShadowDTO;

/**
 * @author luna
 * @date 2024/6/10
 */
public interface ShadowHandler {

    /**
     * 消息处理
     *
     * @param shadowDTO
     */
    String execute(ShadowDTO shadowDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param shadowDTO
     * @return
     */
    boolean isAccept(ShadowDTO shadowDTO);
}
