package com.jobisnvillains.szs.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
public class UserSignupRequestDto {

    @NonNull
    String userId;

    @NonNull
    String password;

    @NonNull
    String name;

    @NonNull
    String regNo;
}
