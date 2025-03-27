package com.ednaldoluiz.websocket.infra.web.useragent;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Getter;

@Getter
public class UserAgentInfo {

    private final Browser browserName;
    private final String browserVersion;
    private final OperatingSystem operatingSystem;
    private final boolean isMobile;

    public UserAgentInfo(UserAgent userAgent) {
        this.browserName = userAgent.getBrowser();
        this.browserVersion = userAgent.getBrowserVersion() != null ? userAgent.getBrowserVersion().getVersion() : "Unknown";
        this.operatingSystem = userAgent.getOperatingSystem();
        this.isMobile = userAgent.getOperatingSystem().getDeviceType() == DeviceType.MOBILE;
    }
}
