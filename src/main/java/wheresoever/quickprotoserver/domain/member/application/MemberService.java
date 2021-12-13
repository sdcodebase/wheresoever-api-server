package wheresoever.quickprotoserver.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.member.domain.Member;
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

    public List<SearchMemberDto> searchMember(Long memberId, String metro, int grade, LocalDate age_from, LocalDate age_to) {
        // 같은 지역, 나이에 맞는 member find
        List<Member> members = memberRepository.searchMemberByOption(metro, age_from, age_to);

        // 위의 멤버들이 보낸 메시지들 가져오기
        List<RandomMessage> messages = randomMessageRepository.findRandomMessagesByGradeIsNotNullAndSenderIn(members);

        //key: memberId, value: averageGrade
        Map<Long, Double> averageGradeMap = getAverageGradeMap(messages);

        // 특정 점수 이상에 맞는 member만 추출
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

        // 팔로워 제외하기
        memberList = memberList.stream()
                .filter(member -> {
                    Long mId = member.getId();
                    return !memberFollowingMap.get(mId);
                })
                .collect(Collectors.toList());

        // key: memberId, value: 멤버를 팔로잉하는 member 숫자
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
