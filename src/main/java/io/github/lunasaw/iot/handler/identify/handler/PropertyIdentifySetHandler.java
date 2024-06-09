package io.github.lunasaw.iot.handler.identify.handler;

import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import io.github.lunasaw.iot.handler.identify.IdentifyHandler;
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
    public String getIdentify() {
        return IotDeviceConstant.Identify.SERVICE_SET;
    }

    @Override
    public OutputParams execute(IdentifyMessageDTO identifyMessageDTO) {
        log.info("设置属性值的地方::execute::identifyMessageDTO = {}", JSON.toJSONString(identifyMessageDTO));
        Map<String, ValueWrapper> wrapperMap = identifyMessageDTO.getData();
        ValueWrapper lightSwitch = wrapperMap.get("LightSwitch");
        return null;
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
