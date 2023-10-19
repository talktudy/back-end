package com.example.talktudy.service.study;

import com.example.talktudy.dto.study.StudyApplyDTO;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.common.ApplyStatus;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.study.StudyMember;
import com.example.talktudy.repository.study.StudyMemberRepository;
import com.example.talktudy.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyMemberService {
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StudyApplyDTO registerStudyApply(Long memberId, Long studyId, String applyText) {
        // 1. DB에서 회원과 스터디를 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));

        // 1-1. 스터디의 개설자가 회원이면 예외 처리
        if (study.getMember() == member) throw new IllegalArgumentException("개설자는 스터디 신청이 불가합니다.");

        // 2. 스터디멤버에서 해당 스터디의 회원이 이미 존재하는지 찾고 존재하면 update, 존재하지 않으면 save
        StudyMember newStudyMember = null;
        boolean isMemberExist = false;

        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudy(study);

        for(StudyMember studyMember : studyMembers) {
            if (studyMember.getMember().equals(member)) { // 존재하면 update
                studyMember.setApplyText(applyText);
                newStudyMember = studyMember;
                isMemberExist = true;
            }
        }

        if (!isMemberExist) { // 존재하지 않으면 save
            newStudyMember = StudyMember.builder()
                    .study(study)
                    .member(member)
                    .applyStatus(ApplyStatus.PENDING)
                    .applyText(applyText)
                    .build();
        }

        newStudyMember = studyMemberRepository.save(newStudyMember);

        return StudyMemberMapper.INSTANCE.studyMemberEntityToDto(
                newStudyMember,
                newStudyMember.getMember().getMemberId(),
                newStudyMember.getMember().getInterests().toString(),
                newStudyMember.getMember().getNickname(),
                newStudyMember.getMember().getProfileImageUrl());
    }

    @Transactional(readOnly = true)
    public List<StudyApplyDTO> getStudyApplyList(Long memberId, Long studyId) {

        // 1. DB에서 회원과 스터디를 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));

        // 2. 해당 회원이 스터디의 개설자인지 확인한다.
        if (!member.equals(study.getMember())) throw new BadCredentialsException("접근 권한이 없습니다.");

        // 3. 스터디 신청 목록을 조회한다.
        List<StudyMember> studyMembers = studyMemberRepository.findAll();

        // 4. Entity -> DTO
        List<StudyApplyDTO> studyApplyResponse = studyMembers.stream()
                .map(
                        studyMember ->
                                StudyMemberMapper.INSTANCE.studyMemberEntityToDto(
                                        studyMember,
                                        studyMember.getMember().getMemberId(),
                                        studyMember.getMember().getInterests().toString(),
                                        studyMember.getMember().getNickname(),
                                        studyMember.getMember().getProfileImageUrl()))
                .collect(Collectors.toList());

        return studyApplyResponse;
    }

    @Transactional
    public StudyApplyDTO changeApplyStatus(Long writerMemberId, Long studyId, Long applyMemberId, String applyStatus) {
        // 1. DB에서 개설자 회원과 스터디를 찾는다.
        Member member = memberRepository.findById(writerMemberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));

        // 2. 해당 회원이 스터디의 개설자인지 확인한다.
        if (!member.equals(study.getMember())) throw new BadCredentialsException("접근 권한이 없습니다.");

        // 3. 신청한 회원이 DB에 존재하는지 확인한다.
        Member applyMember = memberRepository.findById(applyMemberId).orElseThrow(() -> new CustomNotFoundException("신청 회원을 찾을 수 없습니다."));

        // 4. 신청한 회원이 스터디 멤버에 존재하는지 찾고, 스터디 멤버에서 해당 회원의 applyStatus를 변경한다.
        StudyMember newStudyMember = null;

        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudy(study);

        for(StudyMember studyMember : studyMembers) {
            if (studyMember.getMember().equals(applyMember)) {
                // 5. Status에 맞춰 스터디의 현재 인원수를 변경한다.
                ApplyStatus status = Enum.valueOf(ApplyStatus.class, applyStatus);

                switch (status) {
                    case ACCEPTED: // 기존에도 수락됨이었다면, 인원수 변경 X
                        if (studyMember.getApplyStatus() != ApplyStatus.ACCEPTED)
                            study.setCurrentCapacity(study.getCurrentCapacity() + 1);
                        break;
                    case REJECTED: // 거절이면, 인원수 변경 X
                        break;
                    case PENDING: // 수락에서 대기가 오면, 인원수 하락. 거절에서 대기가 오면, 인원수 변경 X
                        if (studyMember.getApplyStatus() == ApplyStatus.ACCEPTED)
                            study.setCurrentCapacity(study.getCurrentCapacity() - 1);
                        break;
                }

                studyMember.setApplyStatus(Enum.valueOf(ApplyStatus.class, applyStatus)); // applyStatus 변경
                newStudyMember = studyMember;

                break;
            }
        }

        studyRepository.save(study);

        // 6. 스터디 멤버를 저장한다.
        newStudyMember = studyMemberRepository.save(newStudyMember);

        return StudyMemberMapper.INSTANCE.studyMemberEntityToDto(
                newStudyMember,
                newStudyMember.getMember().getMemberId(),
                newStudyMember.getMember().getInterests().toString(),
                newStudyMember.getMember().getNickname(),
                newStudyMember.getMember().getProfileImageUrl());

    }
} // end class
