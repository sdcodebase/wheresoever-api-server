package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        if (!isDuplicatedUsername(member.getUsername())) {
            throw new IllegalStateException("이미 존재하는 유저 이름입니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }

    private Boolean isDuplicatedUsername(String username) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        return byUsername.isEmpty();
    }
}
