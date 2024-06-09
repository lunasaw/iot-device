package io.github.lunasaw.iot.listener.handler;

import io.github.lunasaw.iot.domain.NotifyMessageDTO;

/**
 * @author luna
 * @date 2024/6/9
 */
public interface MessageHander {

    /**
     * 消息处理
     *
     * @param notifyMessageDTO
     * @return
     */
    void execute(NotifyMessageDTO notifyMessageDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param notifyMessageDTO
     * @return
     */
    boolean isAccept(NotifyMessageDTO notifyMessageDTO);
}
