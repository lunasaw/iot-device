package io.github.lunasaw.iot.common.constant;

/**
 * @author luna
 * @date 2024/6/8
 */
public interface IotDeviceConstant {

    interface Device {
        String PRODUCT_KEY      = "PRODUCT_KEY";
        String DEVICE_NAME      = "DEVICE_NAME";
        String DEVICE_SECRET    = "DEVICE_SECRET";
        String FIRMWARE_VERSION = "1.0.2";
    }

    interface Identify {
        String SERVICE_SET = "set";
        String SERVICE_GET = "get";
    }
}
