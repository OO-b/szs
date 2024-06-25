package com.jobisnvillains.szs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
public class MemberIncomeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // 사용자아이디
    private String memberName; // 사용자명
    private int comprehensiveIncomeAmount; // 종합소득금액
    private String deductionYear; // 공제년도
    private Double nationalPension;// 국민연금
    private Double creditCard;// 신용카드소득공제
    private Integer taxCredit; // 세액공제

    public MemberIncomeInfo(String userId, String memberName, int comprehensiveIncomeAmount, String deductionYear, Double nationalPension, Double creditCard, Integer taxCredit) {
        this.userId = userId;
        this.memberName = memberName;
        this.comprehensiveIncomeAmount = comprehensiveIncomeAmount;
        this.deductionYear = deductionYear;
        this.nationalPension = nationalPension;
        this.taxCredit = taxCredit;
        this.creditCard = creditCard;
    }
}
