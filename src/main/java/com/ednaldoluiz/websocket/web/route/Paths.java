package com.ednaldoluiz.websocket.web.route;

public interface Paths {

    String API = "/api";
    String APP_V1 = "/v1";
    String APP_V2 = "/v2";
    String CLIENT = API + APP_V1;

    interface V1 {
        interface Auth {
            String AUTH = CLIENT + "/auth";
        }
    }

    interface Auth {
        String LOGIN = "/login";
        String REGISTER = "/register";
        String GENERATE_PASSWORD = "/generate-password";
        String LOGOUT = "/logout";
        String FORGOT_PASSWORD = "/forgot-password";
        String RESET_PASSWORD = "/reset-password";
    }
}
