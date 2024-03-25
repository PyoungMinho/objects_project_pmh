package com.objects.marketbridge.domains.member.service.port;

import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.member.dto.GetMemberInfoWithPassword;
import com.objects.marketbridge.domains.member.dto.MemberEmail;
import com.objects.marketbridge.domains.member.dto.MemberId;

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

    GetMemberInfoWithPassword getMemberInfoById(Long memberId);

    MemberEmail getEmailById(Long memberId);

    MemberId findIdByNameAndEmail(String name, String email);

    MemberEmail findEmailByNameAndPhoneNo(String name, String phoneNo);

}
