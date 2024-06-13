package io.github.lunasaw.iot.gateway.callback;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.dm.api.InitResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.api.IDMCallback;
import com.aliyun.alink.linksdk.tools.AError;

/**
 * 获取子设备物模型回调处理
 * 
 * @author luna
 * @date 2024/6/13
 */
@Slf4j
@Component
public class IotIDMCallback<T> implements IDMCallback<T> {

    @Override
    public void onSuccess(Object o) {
        InitResult initResult = (InitResult)o;
        log.info("onSuccess::o = {}", JSON.toJSONString(initResult));
    }

    @Override
    public void onFailure(AError aError) {

    }
}
