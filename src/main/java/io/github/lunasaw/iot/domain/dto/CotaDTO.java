package io.github.lunasaw.iot.domain.dto;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.dm.shadow.ShadowResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * ota远程配置
 * 
 * @author luna
 * @date 2024/6/9
 */
@AllArgsConstructor
@Data
@Builder
public class CotaDTO {

    private ShadowResponse<String> shadowResponse;

    public CotaDTO(Object data) {
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
