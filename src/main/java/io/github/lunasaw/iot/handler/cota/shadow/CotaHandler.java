package io.github.lunasaw.iot.handler.cota.shadow;

import io.github.lunasaw.iot.domain.dto.CotaDTO;

/**
 * @author luna
 * @date 2024/6/10
 */
public interface CotaHandler {

    /**
     * 消息处理
     *
     * @param cotaDTO
     */
    String execute(CotaDTO cotaDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param cotaDTO
     * @return
     */
    boolean isAccept(CotaDTO cotaDTO);
}
