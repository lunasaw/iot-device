package io.github.lunasaw.iot.domain;

import java.util.ArrayList;
import java.util.List;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.common.enums.ThingTypeEnums;
import io.github.lunasaw.iot.domain.dto.ThingDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luna
 * @date 2024/6/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishMessageDTO {
    private String             deviceKey;

    private List<ThingDataDTO> reportThings = new ArrayList<>();

    public PublishMessageDTO(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public synchronized void addReportThingsProperty(String key, Object data) {
        addReportThingsProperty(key, new ValueWrapper(data));
    }

    public synchronized void addReportThingsProperty(String key, String data) {
        addReportThingsProperty(key, new ValueWrapper(data));
    }

    public synchronized void addReportThingsProperty(String key, ValueWrapper valueWrapper) {
        ThingDataDTO thingDataDTO = new ThingDataDTO();
        thingDataDTO.addValue(key, valueWrapper);
        thingDataDTO.setType(ThingTypeEnums.PROPERTY.getValue());
        reportThings.add(thingDataDTO);
    }

    public synchronized void addReportThingsEvent(String key, ValueWrapper valueWrapper) {
        ThingDataDTO thingDataDTO = new ThingDataDTO();
        thingDataDTO.addValue(key, valueWrapper);
        thingDataDTO.setType(ThingTypeEnums.EVENT.getValue());
        reportThings.add(thingDataDTO);
    }

    public void addReportThingsProperty(ThingDataDTO thingDataDTO) {
        reportThings.add(thingDataDTO);
    }
}
