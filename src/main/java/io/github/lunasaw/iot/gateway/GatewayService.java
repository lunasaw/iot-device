package io.github.lunasaw.iot.gateway;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.dm.api.*;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
import io.github.lunasaw.iot.gateway.callback.IotIDMCallback;
import io.github.lunasaw.iot.handler.sub.SubDeviceActionHandler;
import io.github.lunasaw.iot.listener.IotConnectRrpcListener;
import io.github.lunasaw.iot.listener.sub.IotSubDeviceActionListener;
import io.github.lunasaw.iot.listener.sub.IotSubDeviceConnectListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/13
 */
@Component
@Slf4j
public class GatewayService {

    @Autowired
    private IConnectSendListener         connectSendListener;

    @Autowired
    private IotIDMCallback<InitResult>   iotIDMCallback;

    @Autowired
    private List<SubDeviceActionHandler> subDeviceActionHandlers;

    /**
     * 订阅
     * 
     * @param topic
     * @param deviceInfo
     */
    public void gatewaySubDeviceSubscribe(String topic, DeviceInfo deviceInfo) {
        LinkKit.getInstance().getGateway().gatewaySubDeviceSubscribe(topic, deviceInfo, new IotSubDeviceActionListener(deviceInfo));
    }

    /**
     * 发布
     * 
     * @param topic
     * @param deviceInfo
     * @param publishData
     */
    public void gatewaySubDeviceSubscribe(String topic, DeviceInfo deviceInfo, String publishData) {
        LinkKit.getInstance().getGateway().gatewaySubDevicePublish(topic, publishData, deviceInfo, new IotSubDeviceActionListener(deviceInfo));
    }

    /**
     * 取消订阅
     * 
     * @param topic
     * @param deviceInfo
     */
    public void gatewaySubDeviceUnsubscribe(String topic, DeviceInfo deviceInfo) {
        LinkKit.getInstance().getGateway().gatewaySubDeviceUnsubscribe(topic, deviceInfo, new IotSubDeviceActionListener(deviceInfo));
    }

    /**
     * 子设备物模型销毁，反初始化物模型。后续如果需要重新使用，需重新进行登录、物模型初始化流程。
     * 
     * @param deviceInfo
     */
    public void uninitSubDeviceThing(DeviceInfo deviceInfo) {
        LinkKit.getInstance().getGateway().uninitSubDeviceThing(deviceInfo);
    }

    /**
     * 获取子设备物模型
     * 
     * @param deviceInfo
     * @return
     */
    public IThing getSubDeviceThing(DeviceInfo deviceInfo) {
        Pair<IThing, AError> deviceThing = LinkKit.getInstance().getGateway().getSubDeviceThing(deviceInfo);
        if (deviceThing == null) {
            return null;
        }
        IThing first = deviceThing.first;
        if (first == null) {
            log.error("getSubDeviceThing::deviceInfo = {} , error = {}", deviceInfo, JSON.toJSONString(deviceThing.second));
        }
        return first;
    }

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
     * 网关设备可以在云端控制子设备，如禁用子设备、启用子设备、删除网关与子设备的拓扑关系。目前服务端只支持禁用子设备的下行通知。服务端在禁用子设备时，会对子设备做下线处理，后续网关将不能代理子设备和云端做通信
     * 
     * @param deviceInfo
     * @param iotConnectRrpcListener
     */
    public void gatewaySetSubDeviceDisableListener(DeviceInfo deviceInfo, IotConnectRrpcListener iotConnectRrpcListener) {
        LinkKit.getInstance().getGateway().gatewaySetSubDeviceDisableListener(deviceInfo, iotConnectRrpcListener);
    }

    /**
     * 当子设备离线之后，网关需要通知物联网平台子设备离线，以避免物联网平台向子设备发送数据。子设备下线之后不可以进行子设备的发布、订阅、取消订阅等操作。
     * 
     * @param deviceInfo
     */
    public void gatewaySubDeviceLogout(DeviceInfo deviceInfo) {
        LinkKit.getInstance().getGateway().gatewaySubDeviceLogout(deviceInfo, new IotSubDeviceActionListener(deviceInfo));
    }

    /**
     * 调用子设备上线之前，请确保已完成子设备添加。网关发现子设备连上网关之后，需要通知物联网平台子设备上线，子设备上线后可以执行子设备的订阅、发布等操作。
     * 由于接口调用都是异步的，子设备上线接口不能在子设备添加的相关代码的下一行调用，需在子设备添加成功的回调里面调用。
     * {@link GatewayService#gatewayAddSubDevice(DeviceInfo)} 在这里添加后，IotSubDeviceConnectListener会触发上线
     * 
     * @param deviceInfo
     */
    private void gatewaySubDeviceLogin(DeviceInfo deviceInfo) {
        LinkKit.getInstance().getGateway().gatewaySubDeviceLogin(deviceInfo, new IotSubDeviceActionListener(deviceInfo));
    }

    /**
     * 网关发现并连接了一个新的子设备，并获取到子设备的认证信息后，可以通知物联网平台网关需要添加一个子设备。
     * 网关重启并连接到物联网平台后，对连接的子设备需要再次调用添加子设备方法。
     * 设备添加
     * 
     * @param deviceInfo
     */
    public void gatewayAddSubDevice(DeviceInfo deviceInfo) {
        LinkKit.getInstance().getGateway().gatewayAddSubDevice(deviceInfo, new IotSubDeviceConnectListener(deviceInfo, subDeviceActionHandlers));
    }

    /**
     * 获取子设备列表
     */
    public void gatewayGetSubDevices() {
        LinkKit.getInstance().getGateway().gatewayGetSubDevices(connectSendListener);
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
        if (!LinkKit.getInstance().getDeviceThing().isThingInited()) {
            log.error("device::isThingInited = {} ", false);
            return;
        }
        LinkKit.getInstance().getGateway().gatewaySubDevicRegister(subDeviceList, connectSendListener);
    }

}
