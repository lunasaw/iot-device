package io.github.lunasaw.iot.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import io.github.lunasaw.iot.domain.bo.IotDeviceBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.linkkit.api.*;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.luna.common.check.Assert;

import io.github.lunasaw.iot.common.constant.IotDeviceConstant;
import io.github.lunasaw.iot.domain.bo.IotSubDeviceBO;
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
@EnableConfigurationProperties({IotConfig.class, IotConfig.IotProduct.class, IotDeviceBO.class, IotSubDeviceBO.class})
public class IotDeviceStart implements InitializingBean {

    @Autowired
    private IotConfig               iotConfig;
    @Autowired
    private ILinkKitConnectListener iLinkKitConnectListener;

    public IotDeviceStart(IotConfig iotConfig) {
        this.iotConfig = iotConfig;
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

        if (StringUtils.isNotEmpty(iotConfig.getMqttHostUrl())) {
            // 如果实例详情页面有实例的id, 建议开发者填入实例id. 推荐的做法
            config.channelHost = iotConfig.getMqttHostUrl();
        } else {
            // 如果实例详情页面没有实例的id, 建议开发者填入实例所在的region. 注：该用法不支持深圳和北京两个region
            config.channelHost = productKey + ".iot-as-mqtt." + iotConfig.getRegion() + ".aliyuncs.com:8883";
        }

        /**
         * 是否接受离线消息
         * 对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;

        return config;
    }

    public void init() {
        IotConfig.IotProduct product = iotConfig.getProduct();
        Assert.notNull(product, "product is null");
        IotDeviceBO device = product.getDevice();
        Assert.notNull(device, "device is null");
        String productKey = product.getProductKey();
        String productSecret = product.getProductSecret();
        String deviceName = device.getDeviceName();
        String deviceSecret = device.getDeviceSecret();

        LinkKitInitParams params = new LinkKitInitParams();
        params.mqttClientConfig = getIoTMqttClientConfig(productKey, deviceName, deviceSecret);
        params.deviceInfo = getDeviceInfo(productKey, productSecret, deviceName, deviceSecret);
        params.propertyValues = getPropertyValues();
        params.fmVersion = device.getFirmwareVersion();

        /**
         * 设备进行初始化，并连云
         */
        ILinkKit instance = LinkKit.getInstance();
        instance.init(params, iLinkKitConnectListener);
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
