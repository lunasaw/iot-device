package io.github.lunasaw.iot.cota;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.model.RequestModel;
import com.aliyun.alink.linkkit.api.LinkKit;

import io.github.lunasaw.iot.listener.IotConnectSendListener;

/**
 * @author luna
 * @date 2024/6/10
 */
@Component
public class IotCotaService {

    @Autowired
    private IotConnectSendListener iotConnectSendListener;

    public void cotaGet() {
        RequestModel<Map<String, String>> requestModel = new RequestModel<>();
        requestModel.id = System.currentTimeMillis() + "";
        requestModel.method = "thing.config.get";
        requestModel.version = "1.0";
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("configScope", "product");
        paramsMap.put("getType", "file");
        requestModel.params = paramsMap;
        LinkKit.getInstance().getDeviceCOTA().COTAGet(requestModel, iotConnectSendListener);
    }

}
