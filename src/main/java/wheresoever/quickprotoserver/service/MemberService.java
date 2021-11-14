package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Sex;
import wheresoever.quickprotoserver.repository.member.MemberRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        if (isDuplicatedUsername(member.getEmail())) {
            throw new IllegalStateException("previously exists email");
        }

        memberRepository.save(member);
        return member.getId();
    }

    public Member findMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new IllegalStateException("does not exist member");
        }
        return member.get();
    }

    @Transactional
    public void updateMember(Long memberId, Sex sex, String nickname, LocalDate birthdate, String metropolitan) {
        Optional<Member> optional = memberRepository.findById(memberId);
        if (optional.isEmpty()) {
            throw new IllegalStateException("does not exist member");
        }

        Member member = optional.get();

        if (sex != null) {
            member.setSex(sex);
        }

        if (!nickname.isEmpty()) {
            member.setNickname(nickname);
        }

        if (birthdate != null) {
            member.setBirthdate(birthdate);
        }

        if (!metropolitan.isEmpty()) {
            member.setMetropolitan(metropolitan);
        }
    }

    private Boolean isDuplicatedUsername(String username) {
        Optional<Member> byUsername = memberRepository.findByEmail(username);
        return byUsername.isPresent();
    }

}
