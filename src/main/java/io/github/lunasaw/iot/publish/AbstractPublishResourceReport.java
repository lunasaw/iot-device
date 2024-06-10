package io.github.lunasaw.iot.publish;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.common.iot.enums.ThingTypeEnums;
import io.github.lunasaw.iot.domain.PublishMessageDTO;
import io.github.lunasaw.iot.domain.dto.ThingDataDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 属性上报
 * 
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
public abstract class AbstractPublishResourceReport implements IPublishResourceListener {

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
        LinkKit.getInstance().getDeviceThing().thingEventPost(thingDataDTO.getIdentifier(), params, this);
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
