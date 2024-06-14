package io.github.lunasaw.iot.handler.sub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2024/6/14
 */
@Slf4j
@Component
public class AbstractSubDeviceActionHandler implements SubDeviceActionHandler {
    @Override
    public void execute(String topic, String aMessage) {
        log.info("execute::topic = {}, aMessage = {}", topic, aMessage);
    }

    @Override
    public boolean isAccept(String topic, String aMessage) {
        return false;
    }
}
