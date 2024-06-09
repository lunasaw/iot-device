package io.github.lunasaw.iot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author luna
 * @date 2024/6/9
 */
@AllArgsConstructor
@Builder
@Data
public class PublishResourceDTO {

    private String alinkId;

    private Object object;
}
