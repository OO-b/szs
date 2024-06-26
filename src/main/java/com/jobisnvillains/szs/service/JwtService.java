package com.jobisnvillains.szs.service;

import com.jobisnvillains.szs.domain.Member;
import com.jobisnvillains.szs.repository.MemberRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService {

    private final MemberRepository memberRepository;

    public JwtService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findByUserId(userId);
    }
}
