package io.github.lunasaw.iot.publish;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linkkit.api.ILinkKit;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;

import io.github.lunasaw.iot.common.enums.ThingTypeEnums;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.domain.dto.ThingDataDTO;
import io.github.lunasaw.iot.listener.IotPublishResourceListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 属性上报
 * 
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class ResourceReportService {

    @Autowired
    private IotPublishResourceListener iotPublishResourceListener;

    public void publish(PublishMessageDTO publishMessageDTO) {
        List<ThingDataDTO> reportThings = publishMessageDTO.getReportThings();
        if (CollectionUtils.isEmpty(reportThings)) {
            return;
        }
        for (ThingDataDTO reportThing : reportThings) {
            if (reportThing == null || reportThing.getType() == null) {
                log.info("publish::reportThing = {}", JSON.toJSONString(reportThing));
                continue;
            }
            if (ThingTypeEnums.PROPERTY.getValue().equals(reportThing.getType())) {
                reportProperty(reportThing);
            }

            if (ThingTypeEnums.EVENT.getValue().equals(reportThing.getType())) {
                reportEvent(reportThing);
            }
        }
    }

    /**
     * 发送事件
     * 
     * @param thingDataDTO
     */
    public void reportEvent(ThingDataDTO thingDataDTO) {
        if (thingDataDTO == null) {
            return;
        }
        if (!ThingTypeEnums.EVENT.getValue().equals(thingDataDTO.getType())) {
            return;
        }

        OutputParams params = new OutputParams(thingDataDTO.getValue());
        LinkKit.getInstance().getDeviceThing().thingEventPost(thingDataDTO.getIdentifier(), params, iotPublishResourceListener);
    }
    /**
     * 发送属性
     * 
     * @param thingDataDTO
     */
    public void reportProperty(ThingDataDTO thingDataDTO) {
        if (thingDataDTO == null) {
            return;
        }
        if (!ThingTypeEnums.PROPERTY.getValue().equals(thingDataDTO.getType())) {
            return;
        }
        ILinkKit iLinkKit = LinkKit.getInstance();
        iLinkKit.getDeviceThing().thingPropertyPost(thingDataDTO.getValue(), iotPublishResourceListener);
    }
}
