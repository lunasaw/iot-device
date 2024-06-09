package io.github.lunasaw.iot.publish;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.PublishMessageDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class BasicPublishResourceReport implements IPublishResourceListener {

    public void publish(PublishMessageDTO publishMessageDTO) {
        LinkKit.getInstance().getDeviceThing().thingPropertyPost(publishMessageDTO.getReportData(), this);
    }

    @Override
    public void onSuccess(String s, Object o) {
        log.info("onSuccess::s = {}, o = {}", s, JSON.toJSONString(o));
    }

    @Override
    public void onError(String s, AError aError) {

    }
}
