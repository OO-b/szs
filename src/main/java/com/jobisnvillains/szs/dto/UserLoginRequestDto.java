package com.jobisnvillains.szs.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
public class UserLoginRequestDto {

    @NonNull
    String userId;

    @NonNull
    String password;

}
