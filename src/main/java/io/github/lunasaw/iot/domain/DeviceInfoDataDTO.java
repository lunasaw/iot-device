package io.github.lunasaw.iot.domain;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.DeviceInfo;

public class DeviceInfoDataDTO extends DeviceInfo {

    /**
     * 区域
     */
    public String         region     = "cn-shanghai";
    /**
     * 与网关关联的子设备信息
     * 后续网关测试demo 会 添加子设备 删除子设备 建立 topo关系 子设备上下线等
     */
    public List<BaseInfo> subDevice  = null;
    /**
     * 实例id
     */
    public String         instanceId = "";

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
