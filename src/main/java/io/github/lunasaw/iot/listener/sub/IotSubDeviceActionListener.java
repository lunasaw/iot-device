package io.github.lunasaw.iot.listener.sub;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.tools.AError;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link ISubDeviceChannel} 会触发回调
 * 
 * @author luna
 * @date 2024/6/14
 */
@AllArgsConstructor
@Slf4j
public class IotSubDeviceActionListener implements ISubDeviceActionListener {

    private DeviceInfo info;

    private String     method;

    @Override
    public void onSuccess() {
        log.info("操作成功 onSuccess:: info = {}, method = {}", JSON.toJSONString(info), method);
        // TODO 添加完成后 gatewaySubDeviceLogin
    }

    @Override
    public void onFailed(AError aError) {

    }
}
