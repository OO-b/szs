package com.jobisnvillains.szs.dto.common;

public class TokenResponseDto {

    public String retCd;

    public String accessToken;

    public TokenResponseDto(String retCd, String accessToken) {
        this.retCd = retCd;
        this.accessToken = accessToken;
    }
}
