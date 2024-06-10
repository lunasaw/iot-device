package io.github.lunasaw.iot.listener;

import java.util.List;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;

import io.github.lunasaw.iot.domain.NotifyMessageDTO;
import io.github.lunasaw.iot.handler.notify.MessageNotifyHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 同步服务回调处理函数
 * 同步服务下行方式包括云端系统RRPC下行和用户自定义RRPC下行两种，在该函数中都分别进行处理
 * 设备收到同步服务后，需要通过 MqttRequestService.publish 接口进行及时回复，否则控制台会显示调用超时失败
 *
 * 先注册一个下行数据监听，注册方法请参见认证与连接中连接状态与下行消息监听的notifyListener。
 * 当云端触发服务调用时，用户可以在onNotify收到云端的下行服务调用。
 * 用户收到云端的下行服务调用后，根据实际服务调用对设备做服务处理，处理之后需要设备回复云端的请求，即发布一个带有处理结果的请求到云端。
 * 说明
 * 当前版本添加了支持使用自定义RRPC，云端的同步服务属性下行是通过自定义RRPC通道下行，用户在升级SDK之后要注意这个改动点。
 * 
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class IotConnectNotifyListener implements IConnectNotifyListener, InitializingBean {

    @Autowired
    private List<MessageNotifyHandler> messageNotifyHandlerList;


    public void onNotify(String connectId, String topic, AMessage aMessage) {
        if (connectId == null || org.apache.commons.lang3.StringUtils.isBlank(topic)) {
            return;
        }

        String messageData = new String((byte[])aMessage.getData());
        log.info("onNotify::connectId = {}, topic = {}, aMessage = {}", connectId, topic, messageData);
        NotifyMessageDTO messageDTO = NotifyMessageDTO.builder().connectId(connectId).topic(topic).message(messageData).build();
        for (MessageNotifyHandler messageNotifyHandler : messageNotifyHandlerList) {

            if (messageNotifyHandler.isAccept(messageDTO)) {
                messageNotifyHandler.execute(messageDTO);
            }
        }
    }

    public boolean shouldHandle(String s, String s1) {
        return true;
    }

    public void onConnectStateChange(String s, ConnectState connectState) {}

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("afterPropertiesSet::registerOnNotifyListener");
        LinkKit.getInstance().registerOnNotifyListener(this);
    }
}
