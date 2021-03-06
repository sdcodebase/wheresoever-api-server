package wheresoever.quickprotoserver.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.exception.MemberEmailPreviousExistsException;
import wheresoever.quickprotoserver.domain.member.exception.MemberLoginFailException;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.member.dto.SearchMemberDto;
import wheresoever.quickprotoserver.domain.follow.dao.FollowRepository;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.randommessage.dao.RandomMessageRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RandomMessageRepository randomMessageRepository;
    private final FollowRepository followRepository;

    @Transactional
    public Long join(Member member) {
        if (isDuplicatedEmail(member.getEmail())) {
            throw new MemberEmailPreviousExistsException();
        }
        memberRepository.save(member);
        return member.getId();
    }

    public Member findMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return optionalMember.get();
    }

    @Transactional
    public void updateMember(Long memberId, Sex sex, String nickname, LocalDate birthdate, String metropolitan) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
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

    public Member validateMemberLogin(String email, String password) {
        return memberRepository.findByEmailAndPasswordAndCanceledAtNull(email, password)
                .orElseThrow(MemberLoginFailException::new);
    }

    private Boolean isDuplicatedEmail(String username) {
        Optional<Member> byUsername = memberRepository.findByEmail(username);
        return byUsername.isPresent();
    }

    public List<SearchMemberDto> searchMember(Long memberId, String metro, int grade, LocalDate age_from, LocalDate age_to) {
        // ?????? ??????, ????????? ?????? member find
        List<Member> members = memberRepository.searchMemberByOption(metro, age_from, age_to);

        // ?????? ???????????? ?????? ???????????? ????????????
        List<RandomMessage> messages = randomMessageRepository.findRandomMessagesByGradeIsNotNullAndSenderIn(members);

        //key: memberId, value: averageGrade
        Map<Long, Double> averageGradeMap = getAverageGradeMap(messages);

        // ?????? ?????? ????????? ?????? member??? ??????
        List<Member> memberList =
                members.stream()
                        .filter(member -> {
                            Double average = averageGradeMap.get(member.getId());
                            return Objects.nonNull(average) && average >= grade;
                        })
                        .collect(Collectors.toList());

        HashMap<Long, Boolean> memberFollowingMap = followRepository.getMemberFollowingMap(
                memberId, memberList.stream()
                        .map(Member::getId)
                        .collect(Collectors.toList()));

        // ????????? ????????????
        memberList = memberList.stream()
                .filter(member -> {
                    Long mId = member.getId();
                    return !memberFollowingMap.get(mId);
                })
                .collect(Collectors.toList());

        // key: memberId, value: ????????? ??????????????? member ??????
        HashMap<Long, Integer> followerCountMap =
                followRepository.getFollowerCountMap(
                        memberList.stream()
                                .map(Member::getId)
                                .collect(Collectors.toList())
                );

        return memberList.stream()
                .map(member -> {
                    Long mId = member.getId();

                    Double averageGrade = averageGradeMap.get(mId);
                    Integer followerCount = followerCountMap.get(mId);
                    return new SearchMemberDto(member, averageGrade, followerCount);
                })
                .collect(Collectors.toList());
    }

    private Map<Long, Double> getAverageGradeMap(List<RandomMessage> messages) {
        //key: memberId, value: randomMessage List
        Map<Long, List<RandomMessage>> messagesMap = new HashMap<>();
        messages.forEach(message -> {
            Long senderId = message.getSender().getId();

            if (!messagesMap.containsKey(senderId)) {
                messagesMap.put(senderId, new ArrayList<>(Arrays.asList(message)));
            } else {
                List<RandomMessage> messageList = messagesMap.get(senderId);
                messageList.add(message);
                messagesMap.put(senderId, messageList);
            }
        });

        Map<Long, Double> averageGradeMap = new HashMap<>();
        messages.forEach(message -> {
            Long senderId = message.getSender().getId();
            Double average = messagesMap.get(senderId).stream().mapToInt(RandomMessage::getGrade)
                    .average()
                    .getAsDouble();
            averageGradeMap.put(senderId, average);
        });

        return averageGradeMap;
    }
}
