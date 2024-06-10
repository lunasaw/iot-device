package io.github.lunasaw.iot.handler.notify;

import org.springframework.stereotype.Component;

import io.github.lunasaw.iot.domain.NotifyMessageDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 这里同步处理
 * 
 * @author luna
 * @date 2024/6/9
 */
@Component
@Slf4j
public class PropertyMessageHandler implements MessageNotifyHandler {
    @Override
    public void execute(NotifyMessageDTO notifyMessageDTO) {
        log.info("notify execute::notifyMessageDTO = {}", notifyMessageDTO);
    }

    @Override
    public boolean isAccept(NotifyMessageDTO notifyMessageDTO) {
        return true;
    }
}
