package io.github.lunasaw.iot.listener;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.dto.ResourceRequestDTO;
import io.github.lunasaw.iot.handler.resource.ResourceHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/10
 */
@Slf4j
@Component
public class IotResourceRequestListener implements IResourceRequestListener {

    @Autowired
    private List<ResourceHandler> resourceHandlers;

    @Override
    public void onHandleRequest(AResource aResource, ResourceRequest resourceRequest, IResourceResponseListener iResourceResponseListener) {
        log.info("onHandleRequest::aResource = {}, resourceRequest = {}, iResourceResponseListener = {}", aResource, resourceRequest,
            iResourceResponseListener);

        if (CollectionUtils.isEmpty(resourceHandlers)) {
            return;
        }
        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO(resourceRequest);
        for (ResourceHandler resourceHandler : resourceHandlers) {
            if (resourceHandler.isAccept(resourceRequestDTO)) {
                resourceHandler.execute(resourceRequestDTO);
            }
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(AError aError) {

    }
}
