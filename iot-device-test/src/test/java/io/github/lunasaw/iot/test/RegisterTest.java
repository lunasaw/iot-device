package io.github.lunasaw.iot.test;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttInitParams;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * @author luna
 * @date 2024/6/8
 */
public class RegisterTest {

    public static void main(String[] args) {
        String deviceName = "${YourDeviceName}";
        String productKey = "${YourProductKey}";
        String productSecret = "${YourProductSecret}";

        // 动态注册step1: 确定一型一密的类型（免预注册, 还是非免预注册）
        // case 1: 如果registerType里面填写了regnwl, 表明设备的一型一密方式为免预注册（即无需创建设备）
        // case 2: 如果这个字段为空, 或填写"register", 则表示为需要预注册的一型一密（需要实现创建设备）
        String registerType = "register";

        // 动态注册step2: 设置动态注册的注册接入点域名
        MqttConfigure.mqttHost = "ssl://${YourMqttHostUrl}:8883";

        MqttInitParams initParams = new MqttInitParams(productKey, productSecret, deviceName, "", registerType);

        // 动态注册step3: 如果用户所用的实例为新版本的公共实例或者企业实例（控制台中有实例详情的页面）, 需设置动态注册的实例id
        initParams.instanceId = "${YourInstanceId}";

        final Object lock = new Object();
        LinkKit.getInstance().deviceDynamicRegister(initParams, new IOnCallListener() {
            @Override
            public void onSuccess(com.aliyun.alink.linksdk.channel.core.base.ARequest request,
                com.aliyun.alink.linksdk.channel.core.base.AResponse response) {
                try {
                    String responseData = new String((byte[])response.data);
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    // 一型一密预注册返回
                    String deviceSecret = jsonObject.getString("deviceSecret");

                    // 一型一密免预注册返回
                    String clientId = jsonObject.getString("clientId");
                    String deviceToken = jsonObject.getString("deviceToken");

                    // TODO: 请用户保存用户密钥，不要在此做连云的操作，要等step 4执行完成后再做连云的操作（例如在其onSuccess分支中进行连云）

                    // 让等待的api继续执行
                    synchronized (lock) {
                        lock.notify();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailed(ARequest aRequest, com.aliyun.alink.linksdk.channel.core.base.AError aError) {
                System.out.println("mqtt dynamic registration failed");
                // 让等待的api继续执行
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public boolean needUISafety() {
                return false;
            }
        });

        try {
            // 等待下行报文，一般1s内就有回复
            synchronized (lock) {
                lock.wait(3000);
            }

            // 动态注册step4: 关闭动态注册的实例。
            // 不要在LinkKit.getInstance().deviceDynamicRegister回调中执行下述函数，否则会报错
            LinkKit.getInstance().stopDeviceDynamicRegister(2000, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    System.out.println("mqtt dynamic registration success");
                    // TODO: 在此处参考一机一密进行连云和初始化
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    System.out.println("mqtt dynamic registration failed");
                }
            });

        } catch (Exception e) {
        }
    }
}
