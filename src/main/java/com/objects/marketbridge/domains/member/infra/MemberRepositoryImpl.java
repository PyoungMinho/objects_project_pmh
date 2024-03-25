package com.objects.marketbridge.domains.member.infra;

import com.objects.marketbridge.domains.member.dto.GetMemberInfoWithPassword;
import com.objects.marketbridge.domains.member.dto.MemberEmail;
import com.objects.marketbridge.domains.member.dto.MemberId;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    public Member findByEmail(String email){
        return memberJpaRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Member findById(Long id) {
        return memberJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Member> saveAll(List<Member> members) {
        return memberJpaRepository.saveAll(members);
    }

    @Override
    public Optional<Member> findOptionalByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public Member findByIdWithAddresses(Long id) {
        return memberJpaRepository.findByIdWithAddresses(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public void deleteAllInBatch() {
        memberJpaRepository.deleteAllInBatch();
    }

    @Override
    public GetMemberInfoWithPassword getMemberInfoById(Long memberId) {
        return memberJpaRepository.getMemberInfoById(memberId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberEmail getEmailById(Long memberId) {
        return memberJpaRepository.getEmailById(memberId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberId findIdByNameAndEmail(String name, String email) {
        return memberJpaRepository.findIdByNameAndEmail(name, email).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberEmail findEmailByNameAndPhoneNo(String name, String phoneNo) {
        return memberJpaRepository.findEmailByNameAndPhoneNo(name, phoneNo).orElseThrow(EntityNotFoundException::new);
    }
}
