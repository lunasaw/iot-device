package io.github.lunasaw.iot.handler.identify;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;

import io.github.lunasaw.iot.domain.IdentifyMessageDTO;

/**
 * 消息对应的各个指令，可以在执行后触发回调
 * 
 * @author luna
 * @date 2024/6/9
 */
public interface IdentifyHandler {

    String getIdentify();

    /**
     * 消息处理
     *
     * @param identifyMessageDTO
     * @return
     */
    OutputParams execute(IdentifyMessageDTO identifyMessageDTO);

    /**
     * 该消息是否满足处理条件
     *
     * @param identifyMessageDTO
     * @return
     */
    boolean isAccept(IdentifyMessageDTO identifyMessageDTO);
}
