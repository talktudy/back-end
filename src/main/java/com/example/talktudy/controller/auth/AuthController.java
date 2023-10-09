package com.example.talktudy.controller.auth;

import com.example.talktudy.dto.auth.MemberRequest;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.service.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@ApiOperation("회원 Auth Rest API 컨트롤러")
public class AuthController {
    private final AuthService authService;

    @ApiOperation("회원가입 api - 회원 정보를 입력받아 회원 가입")
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> register(MemberRequest memberRequest) {
        boolean isSuccess = authService.register(memberRequest);

        ResponseDTO responseDTO;

        if (isSuccess) {
            responseDTO = ResponseDTO.builder()
                    .status(HttpStatus.OK)
                    .message("회원가입에 성공했습니다.")
                    .build();
        } else {
            responseDTO = ResponseDTO.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("회원가입에 실패했습니다.")
                    .build();
        }

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }


} // end class
