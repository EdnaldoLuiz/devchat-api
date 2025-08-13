package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CipherPayload(

        @JsonProperty("type")
        int type,

        @JsonProperty("body")
        String body
) {

    public byte[] decodeBody() {
        return java.util.Base64.getDecoder().decode(body);
    }

    @JsonCreator
    public static CipherPayload of(
            @JsonProperty("type") int type,
            @JsonProperty("body") String body
    ) {
        return new CipherPayload(type, body);
    }
}
