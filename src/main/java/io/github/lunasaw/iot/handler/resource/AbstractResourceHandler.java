package io.github.lunasaw.iot.handler.resource;

import io.github.lunasaw.iot.domain.dto.ResourceRequestDTO;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2024/6/13
 */
@Component
public class AbstractResourceHandler implements ResourceHandler {
    @Override
    public String execute(ResourceRequestDTO requestDTO) {
        return "";
    }

    @Override
    public boolean isAccept(ResourceRequestDTO requestDTO) {
        return false;
    }
}
