package com.ednaldoluiz.websocket.web.controller.v1.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {

    private String email;
    private String displayName;
    private String avatar;

}
