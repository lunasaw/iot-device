package io.github.lunasaw.iot.handler.identify;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
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
            error.setCode(100);
            error.setMsg("setPropertyFailed.");
            callback.onComplete(identify, new ErrorInfo(error), null);
        }
    }

    public void onSuccess(Object o, OutputParams outputParams) {
        log.info("onSuccess::o = {}, outputParams = {}", o, outputParams);
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
            if (StringUtils.isNoneBlank(identify)) {
                LinkKit.getInstance().getDeviceThing().setServiceHandler(identify, this);
            }
        }
    }
}
