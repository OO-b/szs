package com.jobisnvillains.szs.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInfo {

    private String userId;
    private String password;

    public static LoginInfo of(String userId, String password) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(userId);
        loginInfo.setPassword(password);
        return loginInfo;
    }
}
