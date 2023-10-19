package com.example.talktudy.service.tag;

import com.example.talktudy.dto.team.TeamResponse;
import com.example.talktudy.repository.team.Team;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamResponse teamEntityToDto(Team team, String tag, String nickname);
} // end class
