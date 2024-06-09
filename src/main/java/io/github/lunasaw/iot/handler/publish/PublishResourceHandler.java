package io.github.lunasaw.iot.handler.publish;

import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.domain.PublishResourceDTO;

/**
 * 推送一个消息，并且处理返回的结果
 * 
 * @author luna
 * @date 2024/6/9
 */
public interface PublishResourceHandler {

    /**
     * 消息推送
     * 
     * @param publishMessageDTO
     */
    void publish(PublishMessageDTO publishMessageDTO);

    /**
     * 消息处理
     *
     * @param publishResourceDTO
     * @return
     */
    void execute(PublishResourceDTO publishResourceDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param publishResourceDTO
     * @return
     */
    boolean isAccept(PublishResourceDTO publishResourceDTO);
}
