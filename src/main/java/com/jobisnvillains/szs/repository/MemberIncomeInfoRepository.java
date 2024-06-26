package com.jobisnvillains.szs.repository;

import com.jobisnvillains.szs.domain.MemberIncomeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberIncomeInfoRepository extends JpaRepository<MemberIncomeInfo, String> {

    Optional<MemberIncomeInfo> findByUserId(String userId);

}
