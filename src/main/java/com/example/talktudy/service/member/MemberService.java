package com.example.talktudy.service.member;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public MemberDTO getMember(Long memberId) {
        // 1. memberId로 DB에서 회원을 찾는다
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. Entity객체를 DTO로 매핑해 리턴한다.
        return MemberMapper.INSTANCE.memberEntityToDto(member);
    }

    @Transactional
    public MemberDTO updateMember(Long memberId, MemberDTO memberDTO) {
        // 1. memberId로 DB에서 회원을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. DTO로 받은 값으로 객체를 수정한다.
        member.setNickname(memberDTO.getNickname() != null ? memberDTO.getNickname() : member.getNickname());
        member.setInterests(Enum.valueOf(Interests.class, memberDTO.getInterests())); // Null 일 수가 없음
        member.setDescription(memberDTO.getDescription() != null ? memberDTO.getDescription() : member.getDescription());

        // 프로파일 수정을 했다면 대체하기
        if (memberDTO.getProfileImage() != null || !memberDTO.getProfileImage().isEmpty()) {
            if (memberDTO.getProfileImage().getOriginalFilename().length() > 0) {
                member.setProfileImageUrl(
                        s3Uploader.uploadFile(memberDTO.getProfileImage(), S3Uploader.S3_DIR_MEMBER)
                );
            }
        }

        if (memberDTO.getPortfolio() != null) {
            if (memberDTO.getPortfolio().getOriginalFilename().length() > 0) {
                member.setPortfolioUrl(s3Uploader.uploadFile(memberDTO.getPortfolio(), S3Uploader.S3_DIR_PORTFOLIO));
            }
        }

        // 3. DB에 저장한 후, 객체를 매핑해 리턴한다.
        Member newMember = memberRepository.save(member);

        return MemberMapper.INSTANCE.memberEntityToDto(newMember);
    }
} // end class
