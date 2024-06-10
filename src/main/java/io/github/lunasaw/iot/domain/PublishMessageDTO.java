package io.github.lunasaw.iot.domain;

import java.util.List;
import java.util.Map;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishMessageDTO {

    private Map<String, ValueWrapper> reportData;

    private List<ThingDataDTO>        reportThings;
}
