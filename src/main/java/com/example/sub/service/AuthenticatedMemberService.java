package com.example.sub.service;

import com.example.sub.domain.entity.Member;
import com.example.sub.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedMemberService {

    private final MemberRepository memberRepository;

    public Member requireMember(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("로그인 회원 정보를 찾을 수 없습니다."));
    }
}
