package io.github.lunasaw.iot.publish;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import io.github.lunasaw.iot.common.iot.enums.ThingTypeEnums;
import io.github.lunasaw.iot.domain.dto.ThingDataDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.PublishMessageDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 属性上报
 * 
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class BasicPublishResourceReport implements IPublishResourceListener {

    public void publish(PublishMessageDTO publishMessageDTO) {

        List<ThingDataDTO> reportThings = publishMessageDTO.getReportThings();
        if (CollectionUtils.isEmpty(reportThings)) {
            return;
        }
        for (ThingDataDTO reportThing : reportThings) {
            reportProperty(reportThing);

        }

        LinkKit.getInstance().getDeviceThing().thingPropertyPost(publishMessageDTO.getReportData(), this);
    }

    public void reportEvent(ThingDataDTO thingDataDTO) {
        if (thingDataDTO == null) {
            return;
        }
        if (!ThingTypeEnums.EVENT.getValue().equals(thingDataDTO.getType())) {
            return;
        }

        OutputParams params = new OutputParams(thingDataDTO.getValue());
        LinkKit.getInstance().getDeviceThing().thingEventPost(thingDataDTO.getIdentifier(), params, this);
    }

    public void reportProperty(ThingDataDTO thingDataDTO) {
        if (thingDataDTO == null) {
            return;
        }
        if (!ThingTypeEnums.PROPERTY.getValue().equals(thingDataDTO.getType())) {
            return;
        }
        LinkKit.getInstance().getDeviceThing().thingPropertyPost(thingDataDTO.getValue(), this);
    }

    @Override
    public void onSuccess(String s, Object o) {
        log.info("onSuccess::s = {}, o = {}", s, JSON.toJSONString(o));
    }

    @Override
    public void onError(String s, AError aError) {

    }
}
