package io.github.lunasaw.iot.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class IotPublishResourceListener implements IPublishResourceListener {
    @Override
    public void onSuccess(String s, Object o) {
        log.info("onSuccess::s = {}, o = {}", s, JSON.toJSONString(o));
    }

    @Override
    public void onError(String s, AError aError) {

    }
}
