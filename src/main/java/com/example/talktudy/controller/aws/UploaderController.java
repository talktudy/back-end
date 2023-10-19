package com.example.talktudy.controller.aws;


import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.uploader.UploaderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequestMapping("/api/upload")
@RestController
@RequiredArgsConstructor
public class UploaderController {
    private final UploaderService uploaderService;

    @ApiOperation("S3 파일 업로드 api - 헤더에 토큰을 받고, S3에 파일을 업로드.")
    @PostMapping()
    public ResponseEntity<ResponseDTO> uploadFile(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestPart("file") MultipartFile file
            ) {
        return ResponseEntity.ok(uploaderService.uploadFile(customUserDetails.getMemberId(), file));
    }
}
