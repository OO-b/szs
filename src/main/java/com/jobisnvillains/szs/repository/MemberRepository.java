package com.jobisnvillains.szs.repository;

import com.jobisnvillains.szs.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);
    List<Member> findAll();
    Optional<Member> findByUserIdAndPassword(String id, String password);
    Optional<Member> findByUserId(String id);

}
