package io.github.lunasaw.iot.handler.identify;

import java.util.List;
import java.util.Map;

import com.luna.common.exception.BaseException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.api.MapInputParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;
import com.google.common.collect.Lists;

import io.github.lunasaw.iot.domain.IdentifyMessageDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 先注册服务的处理监听器，当云端触发异步服务调用时，下行的请求会到注册的监听器中。一个设备会有多种服务，通常需要注册所有服务的处理监听器。
 * onProcess是设备收到的云端下行的服务调用方法，第一个参数是需要调用服务对应的identifier，用户可以根据identifier做不同的处理。云端调用设置服务时，设备需要在收到设置指令后，调用设备执行真实操作，操作结束后上报一条属性状态变化的通知。
 * 说明
 * identifier是在物联网平台为产品创建属性、事件、服务时定义的标识符，用户可在物联网平台控制台查看产品功能定义中属性、事件、服务对应的identifier。
 * 
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class IotResRequestHandler implements ITResRequestHandler {

    @Autowired
    private List<IdentifyHandler> identifyHandlers;

    public void onProcess(String identify, Object result, ITResResponseCallback callback) {
        log.info("onProcess::identify = {}, result = {}", identify, JSON.toJSONString(result));

        Map<String, ValueWrapper> wrapperMap = ((MapInputParams)result).getData();
        IdentifyMessageDTO identifyMessageDTO = IdentifyMessageDTO.builder().identify(identify).data(wrapperMap).build();
        try {
            List<OutputParams> listResult = Lists.newArrayList();
            for (IdentifyHandler identifyHandler : identifyHandlers) {
                if (identifyHandler.isAccept(identifyMessageDTO)) {
                    OutputParams execute = identifyHandler.execute(identifyMessageDTO);
                    if (execute != null) {
                        listResult.add(execute);
                    }
                }
            }

            /**
             * 向云端上报数据
             *
             * errorInfo为空，表示接收数据成功，callback.onComplete回调将
             * 回复/sys/${productKey}/${deviceName}/thing/service/property/set_reply给云端
             * 同时，该回调会再通过/sys/${productKey}/${deviceName}/thing/service/property/post将更新后的属性上报到云端
             * 表示设备端更新该属性成功
             */

            if (CollectionUtils.isNotEmpty(listResult)) {
                for (OutputParams outputParams : listResult) {
                    callback.onComplete(identify, null, outputParams);
                }
            } else {
                callback.onComplete(identify, null, null);
            }

        } catch (Exception e) {
            log.error("onProcess::identify = {}, result = {}", identify, JSON.toJSONString(result), e);
            AError error = new AError();
            error.setCode(BaseException.SYSTEM_ERROR.getCode());
            error.setMsg(BaseException.SYSTEM_ERROR.getMessage());
            callback.onComplete(identify, new ErrorInfo(error), null);
        }
    }

    public void onSuccess(Object o, OutputParams outputParams) {
    }

    public void onFail(Object o, ErrorInfo errorInfo) {
        log.info("onFail::o = {}, errorInfo = {}", o, errorInfo);
    }

    public void setServiceHandler() {
        // 基础命令处理
        List<Service> services = LinkKit.getInstance().getDeviceThing().getServices();
        for (Service service : services) {
            LinkKit.getInstance().getDeviceThing().setServiceHandler(service.getIdentifier(), this);
        }

        // 自定义命令处理
        for (IdentifyHandler identifyHandler : identifyHandlers) {
            String identify = identifyHandler.getIdentify();
            if (StringUtils.isBlank(identify)) {
                continue;
            }
            LinkKit.getInstance().getDeviceThing().setServiceHandler(identify, this);
        }

    }
}
