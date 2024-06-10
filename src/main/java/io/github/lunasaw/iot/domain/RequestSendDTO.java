package io.github.lunasaw.iot.domain;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.tools.AError;

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
public class RequestSendDTO {

    private ARequest  aRequest;
    private AResponse aResponse;
    private AError    aError;
}
