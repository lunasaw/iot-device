package io.github.lunasaw.iot.common.util;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;

/**
 * @author luna
 * @date 2024/6/14
 */
public class MessageUtils {

    public static String getMessage(AMessage message) {
        if (message == null) {
            return null;
        }
        if (message.data instanceof byte[]) {
            return new String((byte[])message.data);
        }
        return String.valueOf(message.data);
    }
}
