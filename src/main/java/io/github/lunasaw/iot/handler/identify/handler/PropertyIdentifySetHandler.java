package io.github.lunasaw.iot.handler.identify.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.common.iot.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.IdentifyMessageDTO;
import io.github.lunasaw.iot.handler.identify.IdentifyHandler;
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
        log.info("设置属性值的地方, 可以在此处回包::execute::identifyMessageDTO = {}", JSON.toJSONString(identifyMessageDTO));
        Map<String, ValueWrapper> wrapperMap = identifyMessageDTO.getData();

        OutputParams outputParams = new OutputParams();
        outputParams.put("LightSwitch", wrapperMap.get("LightSwitch"));
        outputParams.put("LightSwitch2", new ValueWrapper.IntValueWrapper(222));

        return outputParams;
    }

    @Override
    public boolean isAccept(IdentifyMessageDTO identifyMessageDTO) {
        if (identifyMessageDTO == null) {
            return false;
        }
        if (IotDeviceConstant.Identify.SERVICE_SET.equals(identifyMessageDTO.getIdentify())) {
            return true;
        }
        return false;
    }
}
