package io.github.lunasaw.iot.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.api.CommonResource;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
public class MqttRequestService {

    @Autowired
    private IConnectSendListener      iConnectSendListener;

    @Autowired
    private IConnectSubscribeListener subscribeListener;

    @Autowired
    private IConnectUnscribeListener  unscribeListener;

    @Autowired
    private IResourceRequestListener  resourceRequestListener;

    /**
     * 不需要回复的消息
     * 
     * @param topic
     * @param data
     */
    public void publish(String topic, String data) {
        publish(topic, null, data, 0, false);
    }

    /**
     * 带应答的消息
     * 
     * @param topic
     * @param replyTopic
     * @param data
     */
    public void publish(String topic, String replyTopic, String data) {
        publish(topic, replyTopic, data, 0, true);
    }

    /**
     *
     * @param topic 拥有发布权限的Topic。设备通过该Topic向物联网平台发送消息。
     * @param replyTopic 设置物联网平台答复的topic，若不设置，则默认为 topic+“_reply”。
     * @param data 需要发布的数据，可以为任意格式数据。如果格式为JSON String，其中id字段需要保持每次唯一，不可重复，
     * 请使用自增的方式进行设置ID字段。示例中id字段为160865432，则下次id字段应为160865433。
     * eg.
     * {"id":"160865432","method":"thing.event.property.post","params":{"LightSwitch":1},"version":"1.0"}
     * @param qos 设置MQTT请求中QOS的值，默认为0。
     * 0：消息只发一次，不保证设备收到。
     * 1：消息至少发一次，失败重发，保证设备收到消息。
     * @param needRpc 是否为RPC请求，如果是，则需要等待 replyTopic消息后才Rsp。
     * 默认为false，表示不需应答。
     */
    public void publish(String topic, String replyTopic, String data, Integer qos, boolean needRpc) {
        MqttPublishRequest request = new MqttPublishRequest();
        request.topic = topic;
        request.replyTopic = replyTopic;
        request.isRPC = needRpc;
        request.qos = qos;
        request.payloadObj = data;
        publish(request);
    }

    public void publish(MqttPublishRequest request) {
        LinkKit.getInstance().publish(request, iConnectSendListener);
    }

    public void subscribe(String topic, boolean isSubscribe) {
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        request.topic = topic;
        request.isSubscribe = isSubscribe;
        subscribe(request);
    }

    public void subscribe(MqttSubscribeRequest subscribeRequest) {
        LinkKit.getInstance().subscribe(subscribeRequest, subscribeListener);
    }

    public void unSubscribe(String topic) {
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        request.topic = topic;
        request.isSubscribe = false;
        unsubscribe(request);
    }

    public void unsubscribe(MqttSubscribeRequest request) {
        LinkKit.getInstance().unsubscribe(request, unscribeListener);
    }

    public void registerResource(String topic) {
        registerResource(topic, null, false, topic);
    }

    public void registerResource(String topic, Object payload, boolean isNeedAuth, String replyTopic) {
        CommonResource resource = new CommonResource();
        resource.topic = topic;
        resource.payload = payload;
        resource.isNeedAuth = isNeedAuth;
        resource.replyTopic = replyTopic;
        registerResource(resource);
    }

    public void registerResource(CommonResource commonResource) {
        LinkKit.getInstance().registerResource(commonResource, resourceRequestListener);
    }
}
