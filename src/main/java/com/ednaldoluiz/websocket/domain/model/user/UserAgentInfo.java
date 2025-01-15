package com.ednaldoluiz.websocket.domain.model.user;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Getter;

@Getter
public class UserAgentInfo {

    private Browser browserName;
    private String browserVersion;
    private OperatingSystem operatingSystem;
    private boolean isMobile;

    public UserAgentInfo(UserAgent userAgent) {
        this.browserName = userAgent.getBrowser();
        this.browserVersion = userAgent.getBrowserVersion() != null ? userAgent.getBrowserVersion().getVersion() : "Unknown";
        this.operatingSystem = userAgent.getOperatingSystem();
        this.isMobile = userAgent.getOperatingSystem().getDeviceType() == DeviceType.MOBILE;
    }
}
