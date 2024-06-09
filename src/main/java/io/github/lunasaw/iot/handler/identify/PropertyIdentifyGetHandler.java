package io.github.lunasaw.iot.handler.identify;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import io.github.lunasaw.iot.common.iot.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.IdentifyMessageDTO;

import java.util.Map;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class PropertyIdentifyGetHandler implements IdentifyHandler {

    @Override
    public OutputParams execute(IdentifyMessageDTO identifyMessageDTO) {
        log.info("真正获取属性值的地方::execute::identifyMessageDTO = {}", identifyMessageDTO);
        OutputParams outputParams = new OutputParams();
        ValueWrapper.IntValueWrapper intValueWrapper = new ValueWrapper.IntValueWrapper(1);
        outputParams.put("LightSwitch", intValueWrapper);
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
