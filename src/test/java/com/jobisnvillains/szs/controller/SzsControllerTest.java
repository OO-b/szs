package com.jobisnvillains.szs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobisnvillains.szs.domain.AppointedMember;
import com.jobisnvillains.szs.domain.Member;
import com.jobisnvillains.szs.repository.AppointedMemberRepository;
import com.jobisnvillains.szs.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

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
    private final AppointedMemberRepository appointedMemberRepository;

    @Autowired
    SzsControllerTest(SzsController controller, MemberRepository memberRepository, ObjectMapper objectMapper1, AppointedMemberRepository appointedMemberRepository) {
        this.controller = controller;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper1;
        this.appointedMemberRepository = appointedMemberRepository;
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
        member.setUserId("baeiiyy");
        member.setPassword("123456");
        member.setName("관지");
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


        log.info(String.valueOf(mvcResult.getResponse()));


    }


    /**
     * 로그인 테스트
     */
    @Test
    void giveUserInfo_whenUserLogin_thenSuccess() throws Exception {
        Member member = new Member();
        member.setUserId("baeiiyy");
        member.setPassword("123456");
        member.setName("관지");
        member.setRegNo("681108-1582816");

        MvcResult mvcResult = mockMvc.perform(post("/szs/login")
                        .contentType("application/json;charset=UTF-8")
                        .content(toJson(member)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // 저장한 멤버 아이디로 검색


        log.info(String.valueOf(mvcResult.getResponse()));


    }
    

    private <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

}