package io.github.lunasaw.iot.handler.sub;

/**
 * 消息入口
 * 
 * @author luna
 * @date 2024/6/9
 */
public interface SubDeviceActionHandler {

    /**
     * 消息处理
     * 
     * @param topic
     * @param aMessage
     */
    void execute(String topic, String aMessage);

    /**
     * 该消息是否满足处理条件
     *
     * @param topic
     * @return
     */
    boolean isAccept(String topic, String aMessage);
}
