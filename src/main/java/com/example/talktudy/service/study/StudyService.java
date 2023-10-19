package com.example.talktudy.service.study;

import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.repository.chat.ChatRoomRepository;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.study.*;
import com.example.talktudy.repository.tag.Tag;
import com.example.talktudy.repository.tag.TagRepository;
import com.example.talktudy.service.chat.ChatService;
import com.example.talktudy.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {
    private final ChatRoomRepository chatRoomRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final TagService tagService;

    @Transactional
    public StudyResponse registerStudy(Long memberId, StudyRequest studyRequest) {

        // 1. DB에서 회원을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. Study 객체를 만든다.
        Study study = Study.builder()
                .member(member) // 개설자
                .title(studyRequest.getTitle()) // 제목
                .interests(Enum.valueOf(Interests.class, studyRequest.getInterests()))
                .open(true) // 첫 스터디 등록시 무조건 모집중
                .description(studyRequest.getDescription())
                .maxCapacity(studyRequest.getMaxCapacity()) // 모집 인원
                .currentCapacity(0) // 등록시 무조건 0
                .endDate(studyRequest.getEndDate()) // 마감 예정일
                .build();

        // 3. Tag 테이블에서 기존 값이 있는지 검사한다. 값이 있으면 tag에는 저장하고 없으면 저장한다.
        // 그렇게 만들어진 Tag 값을 StudyTag에 저장한다.
        tagService.createStudyTags(studyRequest.getTag().split(","), study);

        // 4. 지원, 팀 채팅방을 개설한다.
        // 지원
        ChatRoom applyChatRoom = ChatRoom.builder()
                .name(study.getTitle())
                .isStudyApply(true)
                .study(study)
                .build();

        // 팀 채팅방
        ChatRoom chatRoom = ChatRoom.builder()
                .name(study.getTitle())
                .isStudyApply(false)
                .study(study)
                .build();

        chatRoomRepository.save(applyChatRoom);
        chatRoomRepository.save(chatRoom);

        // 5. Study 객체를 DB에 등록한다.
        Study newStudy = studyRepository.save(study);

        // 6. Entity -> DTO 매핑한다.
        return StudyMapper.INSTANCE.studyEntityToDto(newStudy, studyRequest.getTag(), member.getNickname());
    }

    @Transactional(readOnly = true)
    public Page<StudyResponse> getStudyList(Pageable pageable, String orderCriteria) {
        Page<Study> studyPage = null;

        if (orderCriteria == null) { // 1. 전체 리스트 조회
            studyPage = studyRepository.findAll(pageable);
        }
        else { // 2. orderCriteria가 내림차 순으로 조회(조회수, 총인원수 등)
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, orderCriteria);
            studyPage = studyRepository.findAll(pageable);
        }

        // 2. 응답 형태로 변환해 리턴한다.
        List<StudyResponse> studyResponses = studyPage.stream()
                .map(study -> StudyMapper.INSTANCE.studyEntityToDto(study, study.getTagNamesAsString(), study.getMember().getNickname()))
                .collect(Collectors.toList());

        return new PageImpl<>(studyResponses, pageable, studyResponses.size());
    }

    @Transactional(readOnly = true)
    public Page<StudyResponse> getStudyListByOpen(Pageable pageable) {

        // 1. isOpen이 true인 모집중인 데이터만 조회
        Page<Study> studyPage = studyRepository.findAllByOpenTrue(pageable);

        // 2. 응답 형태로 변환해 리턴한다.
        List<StudyResponse> studyResponses = studyPage.stream()
                .map(study -> StudyMapper.INSTANCE.studyEntityToDto(study, study.getTagNamesAsString(), study.getMember().getNickname()))
                .collect(Collectors.toList());

        return new PageImpl<>(studyResponses, pageable, studyResponses.size());
    }

    @Transactional(readOnly = true)
    public StudyResponse getStudy(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));
        return StudyMapper.INSTANCE.studyEntityToDto(study, study.getTagNamesAsString(), study.getMember().getNickname());
    }

    @Transactional
    public ResponseDTO closeStudy(Long memberId, Long studyId) {
        // 1. DB에서 개설자 회원과 스터디를 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));

        // 2. 해당 회원이 스터디의 개설자인지 확인한다.
        if (!member.equals(study.getMember())) throw new BadCredentialsException("접근 권한이 없습니다.");

        // 3. 스터디의 마감 상태를 변경한다.
        study.setOpen(false);

        studyRepository.save(study);

        return ResponseDTO.of(200, HttpStatus.OK, "스터디가 모집 완료 상태로 변경되었습니다.");
    }

    // 매일 자정에 실행되며 오늘 자정이 모집 마감일인 스터디 리스트를 찾아 모두 마감시킨다.
    @Scheduled(cron = "0 0 0 * * *")
    public void closeStudyEveryDay() {
        LocalDateTime time = LocalDate.now().atStartOfDay(); // 년,월,일(자정)까지만 가져온다.
        log.info("시간 : " + time);

        List<Study> studies = studyRepository.findAllByEndDate(time);

        if(studies != null) {
            for(Study study : studies) {
                study.setOpen(false);
                log.info("모집 마감되는 스터디 아이디 : " + study.getStudyId());
            }

            studyRepository.saveAll(studies);
        }
    }
} // end class
