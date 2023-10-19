package com.example.talktudy.service.study;

import com.example.talktudy.dto.study.StudyApplyDTO;
import com.example.talktudy.repository.study.StudyMember;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudyMemberMapper {
    StudyMemberMapper INSTANCE = Mappers.getMapper(StudyMemberMapper.class);

    StudyApplyDTO studyMemberEntityToDto(StudyMember studyMember, Long memberId, String interests, String nickname, String profileImageUrl);
}
