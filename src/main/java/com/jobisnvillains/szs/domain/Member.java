package com.jobisnvillains.szs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private String userId;
    private String password;
    private String name;
    private String regNo;


}
