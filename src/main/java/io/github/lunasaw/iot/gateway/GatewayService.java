package io.github.lunasaw.iot.gateway;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.gateway.callback.IotIDMCallback;

/**
 * @author luna
 * @date 2024/6/13
 */
@Component
public class GatewayService {

    @Autowired
    private IConnectSendListener       connectSendListener;

    @Autowired
    private IotIDMCallback<InitResult> iotIDMCallback;

    /**
     * 获取设备物模型
     * 
     * @param deviceInfo
     * @param propertyValues
     */
    public void initSubDeviceThing(DeviceInfo deviceInfo, Map<String, ValueWrapper> propertyValues) {
        initSubDeviceThing(null, deviceInfo, propertyValues);
    }

    /**
     * 获取设备物模型
     * 
     * @param tsl
     * @param deviceInfo
     * @param propertyValues
     */
    public void initSubDeviceThing(String tsl, DeviceInfo deviceInfo, Map<String, ValueWrapper> propertyValues) {
        LinkKit.getInstance().getGateway().initSubDeviceThing(tsl, deviceInfo, propertyValues, iotIDMCallback);
    }

    /**
     * 子设备注册
     * 
     * @param subDeviceList
     */
    public void gatewaySubDeviceRegister(Collection<IotSubDeviceBO> subDeviceList) {
        if (CollectionUtils.isEmpty(subDeviceList)) {
            return;
        }
        List<BaseInfo> collect = subDeviceList.stream().map(IotSubDeviceBO::toDeviceInfo).collect(Collectors.toList());
        gatewaySubDeviceRegister(collect);
    }

    /**
     * 子设备注册
     * 
     * @param subDeviceList
     */
    public void gatewaySubDeviceRegister(List<BaseInfo> subDeviceList) {
        LinkKit.getInstance().getGateway().gatewaySubDevicRegister(subDeviceList, connectSendListener);
    }

}
