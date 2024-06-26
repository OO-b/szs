package com.jobisnvillains.szs.repository;

import com.jobisnvillains.szs.domain.AppointedMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointedMemberRepository extends JpaRepository<AppointedMember, String> {
    Optional<AppointedMember> findByName(String name);
}
