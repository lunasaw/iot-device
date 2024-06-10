package io.github.lunasaw.iot.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.model.RequestModel;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
public class LabelService {

    @Autowired
    private IConnectSendListener iConnectSendListener;

    public void labelDelete(String id, String method, String version, String data) {
        RequestModel<String> requestModel = new RequestModel<>();
        requestModel.id = id;
        requestModel.method = method;
        requestModel.version = version;
        requestModel.params = data;
        labelDelete(requestModel);
    }

    public void labelUpdate(String id, String method, String version, String data) {
        RequestModel<String> requestModel = new RequestModel<>();
        requestModel.id = id;
        requestModel.method = method;
        requestModel.version = version;
        requestModel.params = data;
        labelUpdate(requestModel);
    }

    public void labelDelete(RequestModel requestModel) {
        LinkKit.getInstance().getDeviceLabel().labelDelete(requestModel, iConnectSendListener);
    }

    public void labelUpdate(RequestModel requestModel) {
        LinkKit.getInstance().getDeviceLabel().labelUpdate(requestModel, iConnectSendListener);
    }
}
