package io.github.lunasaw.iot.handler.notify.handler;

import io.github.lunasaw.iot.domain.NotifyMessageDTO;
import io.github.lunasaw.iot.handler.notify.MessageNotifyHandler;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2024/6/9
 */
@Component
public class PropertyMessageHandler implements MessageNotifyHandler {
    @Override
    public void execute(NotifyMessageDTO notifyMessageDTO) {

    }

    @Override
    public boolean isAccept(NotifyMessageDTO notifyMessageDTO) {
        return true;
    }
}
