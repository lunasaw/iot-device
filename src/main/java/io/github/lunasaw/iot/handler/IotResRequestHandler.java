package io.github.lunasaw.iot.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.aliyun.alink.linksdk.tmp.api.InputParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/**
 * @author luna
 * @date 2024/6/9
 */
@Component
public class IotResRequestHandler implements ITResRequestHandler {
    private static final String TAG         = "ThingSample";
    private final static String SERVICE_SET = "set";
    private final static String SERVICE_GET = "get";
    private final static String CONNECT_ID  = "LINK_PERSISTENT";

    public void onProcess(String identify, Object result, ITResResponseCallback itResResponseCallback) {
        ALog.d(TAG, "onProcess() called with: s = [" + identify + "], o = [" + result + "], itResResponseCallback = [" + itResResponseCallback + "]");
        try {
            if (SERVICE_SET.equals(identify)) {

                /**
                 * 云端下发属性，SDK收到后触发的回调
                 *
                 * TODO: 用户需要将下发的属性值，设置到真实设备里面。
                 * 若设置成功，需要将isSetPropertySuccess写为true，
                 * demo将通过itResResponseCallback这个回调，将设备本地更新后的属性值写到云平台，
                 * 云平台的设备详情的物模型数据一栏属性值将会刷新
                 * 若设置失败，需要将isSetPropertySuccess写为false, demo将不更新云平台中的属性值
                 *
                 * 这里假定用户已经将属性设置到真实设备里面，将isSetPropertySuccess写为true
                 */
                boolean isSetPropertySuccess = true;

                if (isSetPropertySuccess) {
                    if (result instanceof InputParams) {
                        Map<String, ValueWrapper> data = (Map<String, ValueWrapper>)((InputParams)result).getData();
                        // 如果控制台下发了属性OverTiltEnable， 可以通过data.get("OverTiltEnable")来获取相应的属性值
                        ALog.d(TAG, "收到下行数据 " + data);

                        /**
                         * 读取属性的值
                         *
                         * 假设用户物模型中有OverCurrentEnable这个属性，并且用户在控制台对OverCurrentEnable进行了下发属性的操作
                         * 我们下面示例代码演示如何从中读取到属性的值
                         *
                         *
                         * TODO:用户需要根据自己的物模型进行适配
                         */

                        // ValueWrapper.IntValueWrapper intValue = (ValueWrapper.IntValueWrapper)
                        // data.get("OverCurrentEnable");
                        // if (null != intValue) {
                        // ALog.d(TAG, "收到下行数据 " + intValue.getValue());
                        // }
                    }

                    /**
                     * 向云端上报数据
                     *
                     * errorInfo为空，表示接收数据成功，itResResponseCallback.onComplete回调将
                     * 回复/sys/${productKey}/${deviceName}/thing/service/property/set_reply给云端
                     * 同时，该回调会再通过/sys/${productKey}/${deviceName}/thing/service/property/post将更新后的属性上报到云端
                     * 表示设备端更新该属性成功
                     */
                    itResResponseCallback.onComplete(identify, null, null);

                } else {
                    AError error = new AError();
                    error.setCode(100);
                    error.setMsg("setPropertyFailed.");
                    itResResponseCallback.onComplete(identify, new ErrorInfo(error), null);
                }

            } else if (SERVICE_GET.equals(identify)) {
                // 初始化的时候将默认值初始化传进来，物模型内部会直接返回云端缓存的值

            } else {
                /**
                 * 异步服务下行处理
                 */
                ALog.d(TAG, "用户根据真实的服务返回服务的值，请参照set示例");
                OutputParams outputParams = new OutputParams();
                // outputParams.put("op", new ValueWrapper.IntValueWrapper(20));
                /**
                 * 设备端接收到服务，并返回响应数据给服务端
                 */
                itResResponseCallback.onComplete(identify, null, outputParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.d(TAG, "TMP 返回数据格式异常");
        }
    }

    public void onSuccess(Object o, OutputParams outputParams) {
        ALog.d(TAG, "onSuccess() called with: o = [" + o + "], outputParams = [" + outputParams + "]");
        ALog.d(TAG, "注册服务成功");
    }

    public void onFail(Object o, ErrorInfo errorInfo) {
        ALog.d(TAG, "onFail() called with: o = [" + o + "], errorInfo = [" + errorInfo + "]");
        ALog.d(TAG, "注册服务失败");
    }
}
