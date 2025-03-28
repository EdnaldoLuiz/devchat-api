package com.ednaldoluiz.websocket.web.useragent;

import eu.bitwalker.useragentutils.UserAgent;

public class UserAgentParser {

    public UserAgentInfo extract(String userAgentString) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return new UserAgentInfo(userAgent);
    }
}