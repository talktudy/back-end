package com.example.talktudy.service.uploader;

import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploaderService {
    private final S3Uploader s3Uploader;

    public ResponseDTO uploadFile(Long memberId, MultipartFile file) {
        // 1. 이미지를 S3에 업로드 한다.
        if (file.isEmpty()) throw new IllegalArgumentException("request에 파일이 없습니다.");

        String fileUrl = s3Uploader.uploadFile(file, S3Uploader.S3_DIR_CONTENT);

        return ResponseDTO.of(200, HttpStatus.OK, fileUrl);
    }
} // end class
