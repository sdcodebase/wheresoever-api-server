package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.Withdrawn;
import wheresoever.quickprotoserver.repository.member.MemberRepository;
import wheresoever.quickprotoserver.repository.withdrawn.WithdrawnRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final WithdrawnRepository withdrawnRepository;

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

    private Boolean isDuplicatedUsername(String username) {
        Optional<Member> byUsername = memberRepository.findByEmail(username);
        return byUsername.isPresent();
    }

    @Transactional
    public Boolean withdrawnMember(Long memberId, String reason) {
        Member member = this.findMember(memberId);
        Withdrawn withdrawn = new Withdrawn(member, reason);
        member.withdraw(withdrawn);

        withdrawnRepository.save(withdrawn);

        return true;
    }

}
