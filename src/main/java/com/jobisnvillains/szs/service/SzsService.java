package com.jobisnvillains.szs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobisnvillains.szs.domain.*;
import com.jobisnvillains.szs.dto.common.BaseResponseDto;
import com.jobisnvillains.szs.repository.AppointedMemberRepository;
import com.jobisnvillains.szs.repository.MemberIncomeInfoRepository;
import com.jobisnvillains.szs.repository.MemberRepository;
import com.jobisnvillains.szs.repository.TaxStandardInfoRepository;
import com.jobisnvillains.szs.util.JWTUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SzsService {

    private final MemberRepository memberRepository;
    private final AppointedMemberRepository appointedMemberRepository;
    private final MemberIncomeInfoRepository memberIncomeInfoRepository;
    private final TaxStandardInfoRepository taxStandardInfoRepository;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;
    private final String scrapUrl;
    private final String xAPIKey;
    private final int maxTaxBase = 1000000000;

    public SzsService(MemberRepository memberRepository, AppointedMemberRepository appointedMemberRepository, MemberIncomeInfoRepository memberIncomeInfoRepository, TaxStandardInfoRepository taxStandardInfoRepository, PasswordEncoder encoder, JWTUtil jwtUtil, @Value("${scrap.url}")String scrapUrl, @Value("${scrap.x-api-key}")String xAPIKey, @Value("${spring.jwt.secret}") String secret) {
        this.memberRepository = memberRepository;
        this.appointedMemberRepository = appointedMemberRepository;
        this.memberIncomeInfoRepository = memberIncomeInfoRepository;
        this.taxStandardInfoRepository = taxStandardInfoRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.scrapUrl = scrapUrl;
        this.xAPIKey = xAPIKey;

    }

    /**
     *  회원가입 서비스
     *
     * @param member {@link Member}
     * @return {@link BaseResponseDto}
     */
    public BaseResponseDto signUp(Member member) throws Exception {

        // 1. 서비스 가입 가능한 회원 인지 확인
        boolean checkResult = checkAvailabilityForMember(member);
        if(!checkResult) return new BaseResponseDto("fail", "회원가입이 불가능합니다.");

        // 2. 회원 정보 저장
        memberRepository.save(member);

        return new BaseResponseDto("success", "회원가입이 완료되었습니다.");
    }

    /**
     *  로그인 서비스
     *
     * @param info {@link LoginInfo}
     * @return {@link Member}
     */
    public String login(LoginInfo info) throws Exception {

        // 가입한 회원 인지 확인
        boolean checkResult = checkIfMemberExists(info.getUserId(), info.getPassword());
        if(!checkResult) return null;

        // 회원 토큰 생성
        String userId = info.getUserId();
        String token = jwtUtil.createJwt(userId);
        return token;
    }

    /**
     *  스크랩 서비스
     *
     * @param userId String
     * @return {@link Member}
     */
    public MemberIncomeInfo scrap(String userId) throws Exception {

        // 회원 이름, 주민번호 추출
        Optional<Member> member = memberRepository.findByUserId(userId);
        String name = member.get().getName();
        Optional<AppointedMember> appMember = appointedMemberRepository.findByName(name);
        String regNo = appMember.get().getRegNo();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new MemberIncomeInquireCondition(name, regNo));

        Connection.Response response = Jsoup.connect(scrapUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .header("X-API-KEY", xAPIKey)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .requestBody(jsonString)
                .method(Connection.Method.POST)
                .timeout(200000)
                .execute();

        Document document = response.parse();
        Element body = document.body();
        String bodyContent = body.text();

        JSONObject jsonObject = new JSONObject(bodyContent);
        JSONObject data = jsonObject.getJSONObject("data");
        Object incomeObj = data.get("종합소득금액");

        Integer comprehensiveIncomeAmount = null;

        if (incomeObj instanceof String) {
            comprehensiveIncomeAmount = Integer.parseInt((String) incomeObj);
        } else if (incomeObj instanceof Integer) {
            comprehensiveIncomeAmount = (int) incomeObj;
        }

        JSONObject incomeDeduction = data.getJSONObject("소득공제");

        JSONArray nationalPensionJson = incomeDeduction.getJSONArray("국민연금");
        Map<String, Double> sumNPSByYear = calculateYearlyTotalNPS(nationalPensionJson);

        JSONObject creditCardDeduction = incomeDeduction.getJSONObject("신용카드소득공제");
        Map<String, Double> sumCreditByYear = calculateTotalCreditDeduction(creditCardDeduction);

        Object taxCreditObj = incomeDeduction.get("세액공제");

        String taxCreditStr = null;
        // 세액공제가 문자열일 경우 그대로 반환
        if (taxCreditObj instanceof String) {
            taxCreditStr = (String) taxCreditObj;
        }

        // 세액공제가 정수일경우 문자열로 변환해서 반환
        else if (taxCreditObj instanceof Integer) {
            taxCreditStr = taxCreditObj.toString();
        }

        int taxCredit = Integer.parseInt(taxCreditStr.replace(",", ""));
        String year = String.valueOf(creditCardDeduction.get("year"));
        Double nationalPension = sumNPSByYear.get(year);
        Double creditCard = sumCreditByYear.get(year);

        MemberIncomeInfo memberIncomeInfo = new MemberIncomeInfo(userId, name, comprehensiveIncomeAmount, year, nationalPension, creditCard, taxCredit);
        MemberIncomeInfo result = memberIncomeInfoRepository.save(memberIncomeInfo);

        return result;

    }

    /**
     *  결정세액 조회 서비스
     *
     * @param userId String
     * @return Integer
     */
    public Integer refund(String userId) {

        Optional<MemberIncomeInfo> memberIncomeInfo = memberIncomeInfoRepository.findByUserId(userId);
        int income = memberIncomeInfo.get().getComprehensiveIncomeAmount(); // 종합소득금액
        int nationPention = (int) Math.round(memberIncomeInfo.get().getNationalPension()); // 국민연금
        int creditCard = (int) Math.round(memberIncomeInfo.get().getCreditCard()); // 국민연금
        int taxCredit = memberIncomeInfo.get().getTaxCredit(); // 세액공제

        // 1. 과세표준 = 종합소득금액 - 소득공제
        int taxBase = income - Math.round(nationPention + creditCard);
        int param = taxBase;

        // 2. 산출세액 = 과제표준 * 기본세율
        if(taxBase > maxTaxBase) param += 1;

        TaxStandardInfo taxStandardInfo = taxStandardInfoRepository.findByIncome(param);
        int taxBaseMin = taxStandardInfo.getTaxBaseMin();
        int standardAmount = taxStandardInfo.getStandardAmount();
        double extraPercent = taxStandardInfo.getExtraPercent() / 100.0;
        int generalRate = (int) Math.round((taxBase - taxBaseMin) * extraPercent);
        int calculatedTax = standardAmount + generalRate;

        //3. 결정세액 = 산출세액 - 세액공제
        int determinedTax = calculatedTax - taxCredit;

        return determinedTax;
    }

    /**
     *  회원 확인
     *
     * @param member {@link Member}
     * @return boolean
     */
    public boolean checkAvailabilityForMember(Member member) throws Exception {

        Optional<AppointedMember> appointedMember = appointedMemberRepository.findByName(member.getName());
        if(appointedMember.isEmpty()) return false;
        return encoder.matches(appointedMember.get().getRegNo(), member.getRegNo());
    }

    /**
     * 회원 여부 확인
     *
     * @param userId
     * @param password
     * @return boolean
     */
    public boolean checkIfMemberExists(String userId, String password){
        Optional<Member> member = memberRepository.findByUserId(userId);
        if(!member.isPresent()) return false;
        return encoder.matches(password, member.get().getPassword());
    }

    // 년도별 합계 계산 함수 (국민연금)
    private static Map<String, Double> calculateYearlyTotalNPS(JSONArray nationalPension) throws JSONException {
        Map<String, Double> sumByYear = new HashMap<>();

        for (int i = 0; i < nationalPension.length(); i++) {
            JSONObject deduction = nationalPension.getJSONObject(i);
            String month = deduction.getString("월");
            String year = month.substring(0, 4); // 월에서 년도 부분 추출
            String deductionStr = deduction.getString("공제액");
            double price = Double.parseDouble(deductionStr.replace(",", ""));

            if (sumByYear.containsKey(year)) {
                double yearSum = sumByYear.get(year);
                sumByYear.put(year, yearSum + price);
            } else {
                sumByYear.put(year, price);
            }
        }

        return sumByYear;
    }

    // 년도별 합계 계산 함수 (신용카드소득공제)
    private static Map<String, Double> calculateTotalCreditDeduction(JSONObject creditCardDeduction) throws JSONException {

        String year = String.valueOf(creditCardDeduction.get("year"));

        JSONArray creditDeduction = creditCardDeduction.getJSONArray("month");

        Map<String, Double> sumByYear = new HashMap<>();

        double sum = 0;
        for (int i = 0; i < creditDeduction.length(); i++) {
            JSONObject deductionMonth = creditDeduction.getJSONObject(i);
            for (String key : deductionMonth.keySet()) {
                String deductionStr = deductionMonth.getString(key);
                double price = Double.parseDouble(deductionStr.replace(",", ""));
                sum += price;
            }
        }
        sumByYear.put(year,sum);

        return sumByYear;
    }

}
