package com.objects.marketbridge.member.service.port;

import com.objects.marketbridge.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    boolean existsByEmail(String email);

    Member findByEmail(String email);

    Optional<Member> findOptionalByEmail(String email);

    Member findById(Long id);

    Member save(Member member);

    List<Member> saveAll(List<Member> members);

    Member findByIdWithAddresses(Long id);

    void deleteAllInBatch();
}
