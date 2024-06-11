package io.github.lunasaw.iot.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.handler.identify.IotResRequestHandler;
import io.github.lunasaw.iot.shadow.IotShadowService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
@Slf4j
public class IotLinkKitConnectListener implements ILinkKitConnectListener {

    @Autowired
    private IotResRequestHandler iotResRequestHandler;

    @Autowired
    private IotShadowService     iotShadowService;

    @Override
    public void onError(AError aError) {
        log.error("onError::aError = {} ", aError);
    }

    @Override
    public void onInitDone(InitResult initResult) {
        iotResRequestHandler.setServiceHandler();
        iotShadowService.setShadowHandlers();
    }
}
