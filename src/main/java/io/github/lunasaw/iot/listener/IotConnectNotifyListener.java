package io.github.lunasaw.iot.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;

import io.github.lunasaw.iot.domain.NotifyMessageDTO;
import io.github.lunasaw.iot.listener.handler.MessageHander;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class IotConnectNotifyListener implements IConnectNotifyListener {

    @Autowired
    private List<MessageHander> messageHanderList;

    public void onNotify(String connectId, String topic, AMessage aMessage) {
        if (connectId == null || org.apache.commons.lang3.StringUtils.isBlank(topic)) {
            return;
        }

        String messageData = new String((byte[])aMessage.getData());
        log.info("onNotify::connectId = {}, topic = {}, aMessage = {}", connectId, topic, messageData);
        NotifyMessageDTO messageDTO = NotifyMessageDTO.builder().connectId(connectId).topic(topic).message(messageData).build();
        for (MessageHander messageHander : messageHanderList) {

            if (messageHander.isAccept(messageDTO)) {
                messageHander.execute(messageDTO);
            }
        }
    }

    public boolean shouldHandle(String s, String s1) {
        return true;
    }

    public void onConnectStateChange(String s, ConnectState connectState) {}
}
