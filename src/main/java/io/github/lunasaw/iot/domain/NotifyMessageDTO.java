package io.github.lunasaw.iot.domain;

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
public class NotifyMessageDTO {

    private String connectId;
    private String topic;
    private String message;
}
