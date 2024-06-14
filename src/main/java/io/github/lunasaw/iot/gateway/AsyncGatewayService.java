package io.github.lunasaw.iot.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;

import io.github.lunasaw.iot.gateway.callback.IotIDMCallback;
import io.github.lunasaw.iot.handler.sub.SubDeviceActionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 同步方法
 * 
 * @author luna
 * @date 2024/6/13
 */
@Component
@Slf4j
public class AsyncGatewayService {

    @Autowired
    private IConnectSendListener         connectSendListener;

    @Autowired
    private IotIDMCallback<InitResult>   iotIDMCallback;

    @Autowired
    private List<SubDeviceActionHandler> subDeviceActionHandlers;

}
