package com.jobisnvillains.szs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobisnvillains.szs.domain.Member;
import com.jobisnvillains.szs.domain.MemberIncomeInfo;
import com.jobisnvillains.szs.dto.common.BaseResponseDto;
import com.jobisnvillains.szs.repository.AppointedMemberRepository;
import com.jobisnvillains.szs.repository.MemberIncomeInfoRepository;
import com.jobisnvillains.szs.repository.MemberRepository;
import com.jobisnvillains.szs.service.SzsService;
import com.jobisnvillains.szs.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
class SzsControllerTest {

    private static MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SzsController controller;
    private final MemberRepository memberRepository;
    private final MemberIncomeInfoRepository memberIncomeInfoRepository;
    private final AppointedMemberRepository appointedMemberRepository;
    private final SzsService szsService;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;


    @Autowired
    SzsControllerTest(SzsController controller, MemberRepository memberRepository, ObjectMapper objectMapper1, MemberIncomeInfoRepository memberIncomeInfoRepository, AppointedMemberRepository appointedMemberRepository, SzsService szsService, PasswordEncoder encoder, JWTUtil jwtUtil) {
        this.controller = controller;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper1;
        this.memberIncomeInfoRepository = memberIncomeInfoRepository;
        this.appointedMemberRepository = appointedMemberRepository;
        this.szsService = szsService;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * 회원가입 성공 테스트
     */
    @Test
    void giveUserInfo_whenUserSignUp_thenSuccess() throws Exception {

        Member member = new Member();
        member.setUserId("tak");
        member.setPassword("123456");
        member.setName("동탁");
        member.setRegNo("921108-1582816");

        MvcResult mvcResult = mockMvc.perform(post("/szs/signup")
                        .contentType("application/json;charset=UTF-8")
                        .content(toJson(member)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // 저장한 멤버 아이디로 검색
        Member findMember = memberRepository.findById(member.getUserId()).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());

        log.info(findMember.toString());
        log.info(findMember.getName());
        log.info(findMember.getPassword());
        log.info(findMember.getRegNo());
        log.info(String.valueOf(mvcResult.getResponse()));

    }

    /**
     * 회원가입 실패 테스트
     */
    @Test
    void giveUserInfo_whenUserSignUp_thenFail() throws Exception {
        Member member = new Member();
        member.setUserId("kw68");
        member.setPassword("123456");
        member.setName("관서");
        member.setRegNo("681108-1582816");

        MvcResult mvcResult = mockMvc.perform(post("/szs/signup")
                        .contentType("application/json;charset=UTF-8")
                        .content(toJson(member)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // 저장한 멤버 아이디로 검색
        Member findMember = memberRepository.findById(member.getUserId()).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());

        log.info(String.valueOf(mvcResult.getResponse()));

    }


    /**
     * 로그인 성공 테스트
     */
    @Test
    void giveUserInfo_whenUserLogin_thenSuccess() throws Exception {

        // 회원가입 초기데이터 입력
        Member insertMember = memberInitialDataInsert();

        Member mem = memberRepository.findById(insertMember.getUserId()).get();

        log.info(mem.toString());
        log.info(mem.getName());
        log.info(mem.getPassword());
        log.info(mem.getRegNo());

        Member member = new Member();
        member.setUserId("tak");
        member.setPassword("123456");
        member.setName("동탁");
        member.setRegNo("921108-1582816");

        MvcResult mvcResult = mockMvc.perform(post("/szs/login")
                        .contentType("application/json;charset=UTF-8")
                        .content(toJson(member)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // 저장한 멤버 아이디로 검색
        Member findMember = memberRepository.findById(member.getUserId()).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());

        log.info(findMember.toString());
        log.info(findMember.getName());
        log.info(findMember.getPassword());
        log.info(findMember.getRegNo());
        log.info(String.valueOf(mvcResult.getResponse()));

    }


    /**
     * 스크래핑 성공 테스트
     */
    @Test
    void giveUserToken_whenUserIncomeScrap_thenSuccess() throws Exception {

        // 회원가입 초기데이터 입력
        Member insertMember = memberInitialDataInsert();

        Member mem = memberRepository.findById(insertMember.getUserId()).get();

        log.info(mem.getName());
        log.info(mem.getPassword());
        log.info(mem.getRegNo());

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJ0YWsiLCJpYXQiOjE3MTkzNzg2MzEsImV4cCI6MTcyMDg0OTg2MH0.i5DUQdeItRvO9xjntABZluN0GHd9_5nNPnmHVae91HE";

         mockMvc.perform(post("/szs/scrap")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String userId = jwtUtil.getUserId(token);
        Optional<MemberIncomeInfo> memberIncomeInfo = memberIncomeInfoRepository.findByUserId(userId);
        Assertions.assertThat(memberIncomeInfo).isNotNull();

    }

    /**
     *  결정세액 조회 API
     *
     */
    @Test
    void giveUserToken_whenUserDeterminedTax_thenSuccess() throws Exception {


        // 회원가입 초기데이터 입력
        Member insertMember = memberInitialDataInsert();

        Member mem = memberRepository.findById(insertMember.getUserId()).get();

        log.info(mem.toString());
        log.info(mem.getName());
        log.info(mem.getPassword());
        log.info(mem.getRegNo());

        szsService.scrap(mem.getUserId());

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJ0YWsiLCJpYXQiOjE3MTkzNzg2MzEsImV4cCI6MTcyMDg0OTg2MH0.i5DUQdeItRvO9xjntABZluN0GHd9_5nNPnmHVae91HE";

        MvcResult mvcResult = mockMvc.perform(post("/szs/refund")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType("application/json;charset=UTF-8")
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.결정세액").exists())
                .andDo(print())
                .andReturn();

        String determinedTaxStr = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);;
        JSONObject jsonObject = new JSONObject(determinedTaxStr);
        Assertions.assertThat(jsonObject.get("결정세액")).isNotNull();


    }



    /**
     * Object to Json
     */
    private <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    private Member memberInitialDataInsert() {
        // 회원가입 초기데이터 입력
        Member insertMember = new Member();
        insertMember.setUserId("tak");
        insertMember.setPassword(encoder.encode("123456"));
        insertMember.setName("동탁");
        insertMember.setRegNo( encoder.encode("921108-1582816"));

        return memberRepository.save(insertMember);

    }

}