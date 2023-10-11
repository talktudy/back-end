package com.example.talktudy.service.member;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.repository.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDTO memberEntityToDto(Member member);
} // end class
