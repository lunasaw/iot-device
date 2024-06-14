package io.github.lunasaw.iot.listener.sub;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.tools.AError;

import lombok.AllArgsConstructor;

/**
 * {@link ISubDeviceChannel} 会触发回调
 * 
 * @author luna
 * @date 2024/6/14
 */
@AllArgsConstructor
public class IotSubDeviceActionListener implements ISubDeviceActionListener {

    private DeviceInfo info;

    @Override
    public void onSuccess() {
        // TODO 添加完成后 gatewaySubDeviceLogin
    }

    @Override
    public void onFailed(AError aError) {

    }
}
