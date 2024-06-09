package io.github.lunasaw.iot.domain;

import java.util.Map;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author luna
 * @date 2024/6/9
 */
@AllArgsConstructor
@Data
@Builder
public class IdentifyMessageDTO {

    private String                    identify;
    /**
     * 消息内容
     */
    private Map<String, ValueWrapper> data;
}
