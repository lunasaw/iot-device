package io.github.lunasaw.iot.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.PublishResourceDTO;
import io.github.lunasaw.iot.handler.publish.PublishResourceHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/9
 */
@Slf4j
@Component
public class IotPublishResourceListener implements IPublishResourceListener {

    @Autowired
    private List<PublishResourceHandler> publishResourceHandlers;

    @Override
    public void onSuccess(String alinkId, Object o) {
        if (StringUtils.isBlank(alinkId)) {
            return;
        }

        log.info("onSuccess::alinkId = {}, o = {}", alinkId, JSON.toJSONString(o));
        PublishResourceDTO resourceDTO = PublishResourceDTO.builder().alinkId(alinkId).object(o).build();
        for (PublishResourceHandler publishResourceHandler : publishResourceHandlers) {

            if (publishResourceHandler.isAccept(resourceDTO)) {
                publishResourceHandler.execute(resourceDTO);
            }
        }
    }

    @Override
    public void onError(String s, AError aError) {

    }
}
