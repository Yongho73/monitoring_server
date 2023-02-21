package kr.co.bizmarvel.ccdm.vo.device;

import lombok.Getter;

@Getter
public class DeviceCodeVo {
    String deviceType;
    String areaCode;
    String exhaustType;

    public DeviceCodeVo(String deviceCode) {
        deviceType = deviceCode.substring(0, 1);
        areaCode = deviceCode.substring(3, 5);
        exhaustType = deviceCode.substring(5, 6);
    }
}
