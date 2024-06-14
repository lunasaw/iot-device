package io.github.lunasaw.iot.listener.sub;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.luna.common.check.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.SignUtils;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tools.AError;
import com.luna.common.text.RandomStrUtil;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.common.util.MessageUtils;
import io.github.lunasaw.iot.handler.sub.SubDeviceActionHandler;
import lombok.AllArgsConstructor;

/**
 * @author luna
 * @date 2024/6/14
 */
@Slf4j
@AllArgsConstructor
public class IotSubDeviceConnectListener implements ISubDeviceConnectListener {

    private static final String          HMACSHA_1 = "hmacsha1";
    private DeviceInfo                   info;

    private List<SubDeviceActionHandler> subDeviceActionHandlers;

    @Override
    public String getSignMethod() {
        // 使用的签名方法
        return HMACSHA_1;
    }

    @Override
    public String getSignValue() {
        // 获取签名，用户使用deviceSecret获得签名结果
        Map<String, String> signMap = new HashMap<>();
        signMap.put(IotDeviceConstant.Device.PRODUCT_KEY, info.productKey);
        signMap.put(IotDeviceConstant.Device.DEVICE_NAME, info.deviceName);
        signMap.put(IotDeviceConstant.Device.CLIENT_ID, getClientId());
        Assert.notNull(info.deviceSecret, "deviceSecret is null");
        return SignUtils.hmacSign(signMap, info.deviceSecret);
    }

    @Override
    public String getClientId() {
        // clientId，可为任意值
        return info.getDevId();
    }

    @Override
    public Map<String, Object> getSignExtraData() {
        return Collections.emptyMap();
    }

    @Override
    public void onConnectResult(boolean isSuccess, ISubDeviceChannel iSubDeviceChannel, AError aError) {
        log.info("onConnectResult::isSuccess = {}, aError = {}", isSuccess, JSON.toJSONString(aError));
        // 添加结果
        if (isSuccess) {
            LinkKit.getInstance().getGateway().gatewaySubDeviceLogin(info, new IotSubDeviceActionListener(info, "gatewaySubDeviceLogin"));
            return;
        }
        log.error("子设备连接失败 onConnectResult::isSuccess = {}, aError = {}", isSuccess, JSON.toJSONString(aError));
    }

    @Override
    public void onDataPush(String s, AMessage aMessage) {
        if (CollectionUtils.isNotEmpty(subDeviceActionHandlers)) {
            return;
        }
        for (SubDeviceActionHandler subDeviceActionHandler : subDeviceActionHandlers) {
            String message = MessageUtils.getMessage(aMessage);
            if (subDeviceActionHandler.isAccept(s, message)) {
                subDeviceActionHandler.execute(s, message);
            }
        }
    }
}
