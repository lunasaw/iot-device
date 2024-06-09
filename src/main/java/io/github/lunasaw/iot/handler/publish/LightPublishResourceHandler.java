package io.github.lunasaw.iot.handler.publish;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linkkit.api.LinkKit;
import io.github.lunasaw.iot.listener.IotPublishResourceListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.domain.PublishResourceDTO;

/**
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class LightPublishResourceHandler implements PublishResourceHandler {

    @Autowired
    private IotPublishResourceListener iotPublishResourceListener;

    @Override
    public void publish(PublishMessageDTO publishMessageDTO) {
        LinkKit.getInstance().getDeviceThing().thingPropertyPost(publishMessageDTO.getReportData(), iotPublishResourceListener);
    }

    @Override
    public void execute(PublishResourceDTO publishResourceDTO) {
        log.info("execute::publishResourceDTO = {}", JSON.toJSONString(publishResourceDTO));
    }

    @Override
    public boolean isAccept(PublishResourceDTO publishResourceDTO) {
        return true;
    }
}
