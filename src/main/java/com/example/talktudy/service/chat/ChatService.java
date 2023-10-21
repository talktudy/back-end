package com.example.talktudy.service.chat;

import com.example.talktudy.dto.chat.ChatRoomDTO;
import com.example.talktudy.exception.CustomNotAcceptException;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.repository.chat.ChatRoomRepository;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.study.StudyMember;
import com.example.talktudy.repository.study.StudyMemberRepository;
import com.example.talktudy.repository.study.StudyRepository;
import com.example.talktudy.repository.team.Team;
import com.example.talktudy.repository.team.TeamMember;
import com.example.talktudy.repository.team.TeamMemberRepository;
import com.example.talktudy.repository.team.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public ChatRoomDTO enterChatRoom(Long memberId, Long teamId, Long studyId, String isStudyApply) {
        // 1. DB에서 회원을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. Team
        if (teamId != null) {
            Team team = teamRepository.findById(teamId).orElseThrow(() -> new CustomNotFoundException("스터디 팀 정보를 찾을 수 없습니다."));

            // 1. 채팅방에서 팀의 인원수를 찾고 현재 인원과 총 인원이 같으면 예외처리
            ChatRoom chatRoom = chatRoomRepository.findByTeam(team).orElseThrow(() -> new CustomNotFoundException("채팅방 정보를 찾을 수 없습니다."));

            if (chatRoom.isCapacityFull()) throw new IllegalArgumentException("현재 채팅방의 인원이 모두 찼습니다.");

            chatRoom.setCurrentCapacity(chatRoom.getCurrentCapacity() + 1);

            ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);

            // 3. entity -> Dto
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomId(chatRoom.getChatRoomId());
            chatRoomDTO.setName(chatRoom.getName());
            chatRoomDTO.setTeamId(teamId);
            chatRoomDTO.setCurrentCapacity(newChatRoom.getCurrentCapacity());
            chatRoomDTO.setMaxCapacity(newChatRoom.getMaxCapacity());

            return chatRoomDTO;
        }

        // 3. Study
        else if (studyId != null && isStudyApply == null) {
            Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));

            // 1. 채팅방에서 스터디의 인원수를 찾고 현재 인원과 총 인원이 같으면 예외처리
            ChatRoom chatRoom = chatRoomRepository.findByStudyAndIsStudyApplyFalse(study).orElseThrow(() -> new CustomNotFoundException("스터디 채팅방 정보를 찾을 수 없습니다."));

            if(chatRoom.isCapacityFull()) throw new IllegalArgumentException("현재 채팅방의 인원이 모두 찼습니다.");

            // 2. 스터디 채팅룸의 현재 인원을 증가시킨다.
            chatRoom.setCurrentCapacity(chatRoom.getCurrentCapacity() + 1);

            ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);

            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomId(chatRoom.getChatRoomId());
            chatRoomDTO.setName(chatRoom.getName());
            chatRoomDTO.setStudyId(studyId);
            chatRoomDTO.setStudyApply(false);
            chatRoomDTO.setCurrentCapacity(newChatRoom.getCurrentCapacity());
            chatRoomDTO.setMaxCapacity(newChatRoom.getMaxCapacity());

            return chatRoomDTO;

        }

        // 스터디 지원
        else if (isStudyApply != null && studyId != null) {
            Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomNotFoundException("스터디 정보를 찾을 수 없습니다."));

            // 1. 채팅방에서 스터디의 인원수를 찾고 현재 인원과 총 인원이 같으면 예외처리
            ChatRoom chatRoom = chatRoomRepository.findByStudyAndIsStudyApplyTrue(study);

            if (chatRoom == null) throw new CustomNotAcceptException("스터디 지원, 신청 Qna 채팅방을 찾을 수 없습니다.");

            if (chatRoom.isCapacityFull()) throw new IllegalArgumentException("현재 채팅방의 인원이 모두 찼습니다.");

            // 2. 스터디 채팅룸의 현재 인원을 증가시킨다.
            chatRoom.setCurrentCapacity(chatRoom.getCurrentCapacity() + 1);

            ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);

            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomId(chatRoom.getChatRoomId());
            chatRoomDTO.setName(chatRoom.getName());
            chatRoomDTO.setStudyId(studyId);
            chatRoomDTO.setStudyApply(false);
            chatRoomDTO.setCurrentCapacity(newChatRoom.getCurrentCapacity());
            chatRoomDTO.setMaxCapacity(newChatRoom.getMaxCapacity());

            return chatRoomDTO;
        }

        return null;
    }

} // end class
