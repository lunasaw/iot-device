package io.github.lunasaw.iot.domain.dto;

import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luna
 * @date 2024/6/9
 */
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ResourceRequestDTO {

    private ResourceRequest request;
}
