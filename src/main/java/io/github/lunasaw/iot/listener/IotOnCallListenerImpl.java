package io.github.lunasaw.iot.listener;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.AResponse;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/8
 */
@Slf4j
@Component
public class IotOnCallListenerImpl implements IOnCallListener {

    final Object lock = new Object();

    @Override
    public void onSuccess(ARequest request, AResponse response) {
        try {
            String responseData = new String((byte[])response.data);
            JSONObject jsonObject = JSONObject.parseObject(responseData);
            // 一型一密预注册返回
            String deviceSecret = jsonObject.getString("deviceSecret");

            // 一型一密免预注册返回
            String clientId = jsonObject.getString("clientId");
            String deviceToken = jsonObject.getString("deviceToken");

            // TODO: 请用户保存用户密钥，不要在此做连云的操作，要等step 4执行完成后再做连云的操作（例如在其onSuccess分支中进行连云）
            // 让等待的api继续执行
            synchronized (lock) {
                lock.notify();
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onFailed(ARequest aRequest, AError aError) {
        log.error("onFailed::aRequest = {}, aError = {} ", aRequest, aError);
    }

    @Override
    public boolean needUISafety() {
        return false;
    }
}
