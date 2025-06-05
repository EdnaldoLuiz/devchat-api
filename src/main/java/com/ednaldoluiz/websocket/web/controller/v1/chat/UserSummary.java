package com.ednaldoluiz.websocket.web.controller.v1.chat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {

    @JsonSerialize(using = ToStringSerializer.class) 
    private Long id;
    private String email;
    private String displayName;
    private String avatar;

}
