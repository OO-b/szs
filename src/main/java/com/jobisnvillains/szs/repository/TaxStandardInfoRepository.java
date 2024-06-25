package com.jobisnvillains.szs.repository;

import com.jobisnvillains.szs.domain.TaxStandardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaxStandardInfoRepository extends JpaRepository<TaxStandardInfo, String> {

    @Query("SELECT tsi FROM TaxStandardInfo tsi WHERE tsi.taxBaseMin < :taxStandard AND tsi.taxBaseMax >= : taxStandard")
    TaxStandardInfo findByIncome(@Param("taxStandard")int income);

}
