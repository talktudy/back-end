package com.example.talktudy.service.study;

import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.study.StudyTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudyMapper {
    StudyMapper INSTANCE = Mappers.getMapper(StudyMapper.class);

    // source는 엔티티, target은 dto
    StudyResponse studyEntityToDto(Study study, String tag, String nickname);
}
