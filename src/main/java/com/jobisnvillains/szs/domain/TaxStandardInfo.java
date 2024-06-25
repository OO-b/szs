package com.jobisnvillains.szs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class TaxStandardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int taxBaseMin; // 과세표준 최소값
    private int taxBaseMax; // 과세표준 최대값
    private int standardAmount; // 기본세율 기준금액
    private int extraPercent; // 추가 계산 퍼센트

    public TaxStandardInfo(int taxBaseMin, int taxBaseMax, int standardAmount, int extraPercent) {
        this.taxBaseMin = taxBaseMin;
        this.taxBaseMax = taxBaseMax;
        this.standardAmount = standardAmount;
        this.extraPercent = extraPercent;
    }

    public static List<TaxStandardInfo> of(){
        List<TaxStandardInfo> taxStandardInfoList = new ArrayList<>();
        taxStandardInfoList.add(new TaxStandardInfo(0, 14000000, 0, 6));
        taxStandardInfoList.add(new TaxStandardInfo(14000000, 50000000, 840000, 15));
        taxStandardInfoList.add(new TaxStandardInfo(50000000, 88000000, 6240000, 24));
        taxStandardInfoList.add(new TaxStandardInfo(88000000, 150000000, 1536000, 35));
        taxStandardInfoList.add(new TaxStandardInfo(150000000, 300000000, 3706000, 38));
        taxStandardInfoList.add(new TaxStandardInfo(300000000, 500000000, 9406000, 40));
        taxStandardInfoList.add(new TaxStandardInfo(500000000, 1000000000, 1740600, 42));
        taxStandardInfoList.add(new TaxStandardInfo(1000000000, 999999999, 3840600, 45));

        return taxStandardInfoList;
    }

}
