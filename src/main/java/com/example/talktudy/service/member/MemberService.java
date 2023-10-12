package com.example.talktudy.service.member;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.dto.auth.PasswordRequest;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
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

    @Transactional
    public ResponseDTO changePassword(Long memberId, String oldPassword, String newPassword) {
        // 1. memberId로 DB에서 회원을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 1-1. 패스워드를 입력하지 않았을 경우
        if (oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("패스워드를 모두 입력하지 않았습니다.");
        }

        // 2. 회원의 비밀번호를 찾아 암호화된 비밀번호가 일치하는지 확인한다.
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 새 비밀번호로 변경한다.
        member.setPassword(passwordEncoder.encode(newPassword));

        memberRepository.save(member);

        return ResponseDTO.of(200, HttpStatus.OK, "비밀번호 변경에 성공했습니다.");
    }
} // end class
