package io.github.lunasaw.iot.shadow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;

import io.github.lunasaw.iot.listener.IotShadowRrpcListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
@Order(9)
public class IotShadowService {

    @Autowired
    private IConnectSendListener  iConnectSendListener;

    @Autowired
    private IotShadowRrpcListener iotShadowRRPCListener;

    /**
     * 上报影子
     * 
     * @param value
     */
    public void shadowUpload(String value) {
        LinkKit.getInstance().getDeviceShadow().shadowUpload(value, iConnectSendListener);
    }

    public void setShadowHandlers() {
        LinkKit.getInstance().getDeviceShadow().setShadowChangeListener(iotShadowRRPCListener);
    }
}
