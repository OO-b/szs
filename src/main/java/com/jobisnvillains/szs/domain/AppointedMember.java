package com.jobisnvillains.szs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointedMember {

    @Id
    private String name;
    private String regNo;

    public static List<AppointedMember> of(){
        List<AppointedMember> appointedMemberList = new ArrayList<>();
        appointedMemberList.add(new AppointedMember("동탁","921108-1582816"));
        appointedMemberList.add(new AppointedMember("관우","681108-1582816"));
        appointedMemberList.add(new AppointedMember("손권","890601-2455116"));
        appointedMemberList.add(new AppointedMember("유비","790411-1656116"));
        appointedMemberList.add(new AppointedMember("조조","810326-2715702"));
        return appointedMemberList;
    }

}
