package com.jobisnvillains.szs.common;

import com.jobisnvillains.szs.domain.AppointedMember;
import com.jobisnvillains.szs.domain.TaxStandardInfo;
import com.jobisnvillains.szs.repository.AppointedMemberRepository;
import com.jobisnvillains.szs.repository.TaxStandardInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SzsApplicationRunner implements ApplicationRunner {

    private final AppointedMemberRepository appointedMemberRepository;
    private final TaxStandardInfoRepository taxStandardInfoRepository;

    public SzsApplicationRunner(AppointedMemberRepository appointedMemberRepository, TaxStandardInfoRepository taxStandardInfoRepository) {
        this.appointedMemberRepository = appointedMemberRepository;
        this.taxStandardInfoRepository = taxStandardInfoRepository;
    }

    /**
     *  Spring Boot 구동시 초기 DB 데이터 입력
     *
     * @param args ApplicationArguments
     */
    @Override
    public void run(ApplicationArguments args){
        log.info("Initial Setting");
        prepareAppointedMembers();
        prepareTaxStandardInfo();
    }

    /**
     *  회원 가입 가능한 회원 테이블 구성
     *
     */
    public void prepareAppointedMembers() {
        List<AppointedMember> appointedMemberList = AppointedMember.of();
        for(AppointedMember appointedMember : appointedMemberList) {
            appointedMemberRepository.save(appointedMember);
        }
    }


    /**
     *  세액 기준 테이블 구성
     *
     */
    public void prepareTaxStandardInfo() {
        List<TaxStandardInfo> appointedMemberList = TaxStandardInfo.of();
        for(TaxStandardInfo appointedMember : appointedMemberList) {
            taxStandardInfoRepository.save(appointedMember);
        }
    }
}
