package io.github.lunasaw.iot.handler.notify;

import io.github.lunasaw.iot.domain.NotifyMessageDTO;

/**
 * 消息入口
 * 
 * @author luna
 * @date 2024/6/9
 */
public interface MessageNotifyHandler {

    /**
     * 消息处理
     *
     * @param notifyMessageDTO
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
