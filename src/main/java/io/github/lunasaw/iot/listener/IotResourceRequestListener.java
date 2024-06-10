package io.github.lunasaw.iot.listener;

import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener;
import com.aliyun.alink.linksdk.tools.AError;
import io.github.lunasaw.iot.mqtt.MqttRequestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class IotResourceRequestListener implements IResourceRequestListener {

    @Autowired
    private MqttRequestService requestService;

    @Override
    public void onHandleRequest(AResource aResource, ResourceRequest resourceRequest, IResourceResponseListener iResourceResponseListener) {
        // 下行数据解析示例
        String downstreamData = new String((byte[])resourceRequest.payloadObj);
        // 示例
        // {"id":"269297015","version":"1.0","method":"thing.event.property.post","params":{"lightData":{"vv":12}}}
        log.info("onHandleRequest::aResource = {}, resourceRequest = {}, downstreamData = {}", aResource, resourceRequest.topic, downstreamData);

        // 如果数据是json，且包含id字段，格式可以按照如下示例回复，传输数据请根据实际情况定制
        // if (aResource instanceof CommonResource) {
        // ((CommonResource) aResource).replyTopic = resourceRequest.topic;
        // }
        // if (iResourceResponseListener != null) {
        // AResponse response = new AResponse();
        //
        // response.data = "{\"id\":\"123\", \"code\":\"200\"" + ",\"data\":{} }";
        // iResourceResponseListener.onResponse(aResource, resourceRequest, response);
        // }
        // 如果不一定是json格式，可以参考如下方式回复
        requestService.publish(resourceRequest.topic, StringUtils.EMPTY);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(AError aError) {

    }
}
