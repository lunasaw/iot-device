package io.github.lunasaw.iot.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.linkkit.api.*;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * @author luna
 * @date 2024/6/7
 */
@Component
@Data
@Slf4j
@Configuration
@EnableConfigurationProperties({AliyunIotConfig.class, AliyunIotProduct.class, AliyunIotDevice.class})
public class AliyunIotDeviceStart implements InitializingBean {

    public static Map<String, ILinkKit> DEVICES = new HashMap<>();
    @Autowired
    private AliyunIotConfig             aliyunIotConfig;
    @Autowired
    private ILinkKitConnectListener     iLinkKitConnectListener;

    public AliyunIotDeviceStart(AliyunIotConfig aliyunIotConfig) {
        this.aliyunIotConfig = aliyunIotConfig;
    }

    public Map<String, ValueWrapper> getPropertyValues() {
        /**
         * 设置设备当前的初始状态值，属性需要和云端创建的物模型属性一致
         * 如果这里什么属性都不填，物模型就没有当前设备相关属性的初始值。
         * 用户调用物模型上报接口之后，物模型会有相关数据缓存。
         */
        return new HashMap<>();
    }

    private DeviceInfo getDeviceInfo(String productKey, String productSecret, String deviceName, String deviceSecret) {
        /**
         * 设置初始化三元组信息，用户传入
         */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = productKey;
        deviceInfo.deviceName = deviceName;
        deviceInfo.deviceSecret = deviceSecret;
        deviceInfo.productSecret = productSecret;
        return deviceInfo;
    }

    public IoTMqttClientConfig getIoTMqttClientConfig(String productKey, String deviceName, String deviceSecret) {
        /**
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        if (StringUtils.isBlank(deviceSecret)) {
            deviceSecret = System.getenv(IotDeviceConstant.Device.DEVICE_SECRET);
        }
        config.productKey = productKey;
        config.deviceName = deviceName;
        config.deviceSecret = deviceSecret;

        if (StringUtils.isNotEmpty(aliyunIotConfig.getMqttHostUrl())) {
            // 如果实例详情页面有实例的id, 建议开发者填入实例id. 推荐的做法
            config.channelHost = aliyunIotConfig.getMqttHostUrl();
        } else {
            // 如果实例详情页面没有实例的id, 建议开发者填入实例所在的region. 注：该用法不支持深圳和北京两个region
            config.channelHost = productKey + ".iot-as-mqtt." + aliyunIotConfig.getRegion() + ".aliyuncs.com:8883";
        }

        /**
         * 是否接受离线消息
         * 对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;

        return config;
    }

    public void init() {

        List<AliyunIotProduct> productList = aliyunIotConfig.getProductList();
        if (CollectionUtils.isEmpty(productList)) {
            return;
        }
        for (AliyunIotProduct aliyunIotProduct : productList) {
            LinkKitInitParams params = new LinkKitInitParams();
            if (CollectionUtils.isEmpty(aliyunIotProduct.getDeviceList())) {
                return;
            }
            for (AliyunIotDevice aliyunIotDevice : aliyunIotProduct.getDeviceList()) {
                String productKey = aliyunIotProduct.getProductKey();
                String productSecret = aliyunIotProduct.getProductSecret();
                String deviceName = aliyunIotDevice.getDeviceName();
                String deviceSecret = aliyunIotDevice.getDeviceSecret();

                params.mqttClientConfig = getIoTMqttClientConfig(productKey, deviceName, deviceSecret);

                params.deviceInfo = getDeviceInfo(productKey, productSecret, deviceName, deviceSecret);

                params.propertyValues = getPropertyValues();

                params.fmVersion = IotDeviceConstant.Device.FIRMWARE_VERSION;

                /**
                 * 设备进行初始化，并连云
                 */
                ILinkKit instance = LinkKit.getInstance();
                instance.init(params, iLinkKitConnectListener);
                DEVICES.put(productKey + "-" + deviceName, instance);
            }
        }

    }

    @PreDestroy
    public void preDestroy() {
        LinkKit.getInstance().deinit();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
