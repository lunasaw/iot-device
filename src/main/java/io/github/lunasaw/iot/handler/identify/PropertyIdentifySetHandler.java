package io.github.lunasaw.iot.handler.identify;

import java.util.Map;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.common.iot.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.IdentifyMessageDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class PropertyIdentifySetHandler implements IdentifyHandler {

    @Override
    public OutputParams execute(IdentifyMessageDTO identifyMessageDTO) {
        log.info("初始化属性值的地方::execute::identifyMessageDTO = {}", identifyMessageDTO);
        Map<String, ValueWrapper> wrapperMap = identifyMessageDTO.getData();
        ValueWrapper lightSwitch = wrapperMap.get("LightSwitch");
        return null;
    }

    @Override
    public boolean isAccept(IdentifyMessageDTO identifyMessageDTO) {
        if (identifyMessageDTO == null) {
            return false;
        }
        if (IotDeviceConstant.Identify.SERVICE_GET.equals(identifyMessageDTO.getIdentify())) {
            return true;
        }
        return false;
    }
}
