package com.ednaldoluiz.websocket.shared.utils;

import com.ednaldoluiz.websocket.domain.model.user.UserAgentInfo;
import eu.bitwalker.useragentutils.UserAgent;

public class WebUtils {

    /**
     * Extrai informações do User Agent a partir de uma string.
     *
     * @param userAgentString String do User Agent enviada pelo cliente.
     * @return UserAgentInfo com as informações extraídas.
     */
    public UserAgentInfo extract(String userAgentString) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
        return new UserAgentInfo(userAgent);
    }
}