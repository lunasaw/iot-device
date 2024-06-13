package io.github.lunasaw.iot.common.constant;

/**
 * @author luna
 * @date 2024/6/8
 */
public interface IotDeviceConstant {

    interface Device {
        String PRODUCT_KEY      = "productKey";
        String DEVICE_NAME      = "deviceName";
        String DEVICE_SECRET    = "DEVICE_SECRET";
        String FIRMWARE_VERSION = "1.0.2";
    }

    interface Identify {
        String SERVICE_SET = "set";
        String SERVICE_GET = "get";
    }

    interface Topic {

        String POST_REPLY = "/sys/{productKey}/{deviceName}/thing/event/property/post_reply";
    }

    interface Method {
        String THING_SUB_REGISTER = "thing.sub.register";
    }
}
