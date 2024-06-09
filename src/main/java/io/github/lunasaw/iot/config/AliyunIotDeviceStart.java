package io.github.lunasaw.iot.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.aliyun.alink.linksdk.tools.ALog;
import io.github.lunasaw.iot.handler.IotResRequestHandler;
import io.github.lunasaw.iot.listener.IotConnectNotifyListener;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;

import io.github.lunasaw.iot.common.iot.constant.IotDeviceConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luna
 * @date 2024/6/7
 */
@Component
@Data
@Slf4j
@Configuration
@EnableConfigurationProperties(AliyunIotConfig.class)
public class AliyunIotDeviceStart implements InitializingBean {

    @Autowired
    private AliyunIotConfig     aliyunIotConfig;

    @Autowired
    private IOnCallListener     iOnCallListener;

    @Autowired
    private IMqttActionListener iMqttActionListener;

    public AliyunIotDeviceStart(AliyunIotConfig aliyunIotConfig) {
        this.aliyunIotConfig = aliyunIotConfig;
    }

    public void init() {
        LinkKitInitParams params = new LinkKitInitParams();

        String ps = aliyunIotConfig.getProductSecret();
        if (StringUtils.isBlank(ps)) {
            ps = System.getenv(IotDeviceConstant.Device.DEVICE_SECRET);
        }
        /**
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = aliyunIotConfig.getProductKey();
        config.deviceName = aliyunIotConfig.getDeviceName();
        config.deviceSecret = ps;

        if (!aliyunIotConfig.getIotInstanceId().isEmpty()) {
            // 如果实例详情页面有实例的id, 建议开发者填入实例id. 推荐的做法
            config.channelHost = aliyunIotConfig.getMqttHostUrl();
        } else {
            // 如果实例详情页面没有实例的id, 建议开发者填入实例所在的region. 注：该用法不支持深圳和北京两个region
            config.channelHost = aliyunIotConfig.getProductKey() + ".iot-as-mqtt." + aliyunIotConfig.getRegion() + ".aliyuncs.com:8883";
        }

        /**
         * 是否接受离线消息
         * 对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;

        /**
         * 设置初始化三元组信息，用户传入
         */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = aliyunIotConfig.getProductKey();
        deviceInfo.deviceName = aliyunIotConfig.getDeviceName();
        deviceInfo.deviceSecret = ps;

        params.deviceInfo = deviceInfo;

        /**
         * 设置设备当前的初始状态值，属性需要和云端创建的物模型属性一致
         * 如果这里什么属性都不填，物模型就没有当前设备相关属性的初始值。
         * 用户调用物模型上报接口之后，物模型会有相关数据缓存。
         */
        Map<String, ValueWrapper> propertyValues = new HashMap<>();
        params.propertyValues = propertyValues;
        params.fmVersion = IotDeviceConstant.Device.FIRMWARE_VERSION;

        /**
         * 设备进行初始化，并连云
         */
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            @Override
            public void onError(AError aError) {
                log.error("onError::aError = {} ", aError);
            }

            @Override
            public void onInitDone(InitResult initResult) {
                setServiceHandler();
            }
        });
    }

    @Autowired
    private IotResRequestHandler     resRequestHandler;

    @Autowired
    private IotConnectNotifyListener iotConnectNotifyListener;

    /**
     * 设备端接收服务端的属性下发和服务下发的消息，并作出反馈
     */
    public void setServiceHandler() {
        ALog.d("TAG", "setServiceHandler() called");
        List<Service> srviceList = LinkKit.getInstance().getDeviceThing().getServices();
        for (int i = 0; srviceList != null && i < srviceList.size(); i++) {
            Service service = srviceList.get(i);
            LinkKit.getInstance().getDeviceThing().setServiceHandler(service.getIdentifier(), resRequestHandler);
        }
        LinkKit.getInstance().registerOnNotifyListener(iotConnectNotifyListener);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
