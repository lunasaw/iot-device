package io.github.lunasaw.iot.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.api.MapInputParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.common.collect.Lists;

import io.github.lunasaw.iot.domain.IdentifyMessageDTO;
import io.github.lunasaw.iot.handler.identify.IdentifyHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class IotResRequestHandler implements ITResRequestHandler {

    private static final String   TAG        = "ThingSample";
    private final static String   CONNECT_ID = "LINK_PERSISTENT";
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
                    break;
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
        ALog.d(TAG, "onSuccess() called with: o = [" + o + "], outputParams = [" + outputParams + "]");
        ALog.d(TAG, "注册服务成功");
    }

    public void onFail(Object o, ErrorInfo errorInfo) {
        ALog.d(TAG, "onFail() called with: o = [" + o + "], errorInfo = [" + errorInfo + "]");
        ALog.d(TAG, "注册服务失败");
    }
}
