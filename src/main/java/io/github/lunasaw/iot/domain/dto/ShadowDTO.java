package io.github.lunasaw.iot.domain.dto;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.aliyun.alink.dm.shadow.ShadowResponse;
import com.aliyun.alink.linksdk.tools.ALog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * @author luna
 * @date 2024/6/9
 */
@AllArgsConstructor
@Data
@Builder
public class ShadowDTO {

    private ShadowResponse<String> shadowResponse;

    public ShadowDTO(Object data) {
        String dataStr;
        if (data instanceof byte[]) {
            dataStr = new String((byte[])data, StandardCharsets.UTF_8);
        } else if (data instanceof String) {
            dataStr = (String)data;
        } else {
            dataStr = String.valueOf(data);
        }
        this.shadowResponse = JSONObject.parseObject(dataStr, new TypeReference<ShadowResponse<String>>() {}.getType());
    }
}
