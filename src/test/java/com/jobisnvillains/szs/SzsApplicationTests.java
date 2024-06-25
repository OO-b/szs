package com.jobisnvillains.szs;

import com.jobisnvillains.szs.domain.Member;
import com.jobisnvillains.szs.repository.MemberRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

@Slf4j
@SpringBootTest
class SzsApplicationTests {

	private final MemberRepository memberRepository;

	@Autowired
    SzsApplicationTests(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

	@Test
	void giveUserInfo_whenUserSignUp_thenSuccess() {
		Member member = new Member();
		member.setUserId("kw68");
		member.setPassword("123456");
		member.setName("관우");
		member.setRegNo("681108-1582816");

		memberRepository.save(member);

		// 저장한 멤버 아이디로 검색
		Member findMember = memberRepository.findById(member.getUserId()).get();
		Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());

		log.info(findMember.toString());
		log.info(findMember.getName());
		log.info(findMember.getPassword());
		log.info(findMember.getRegNo());

	}

}
