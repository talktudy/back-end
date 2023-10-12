package com.example.talktudy.controller.auth;

import com.example.talktudy.dto.auth.LoginRequest;
import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.dto.auth.TokenDTO;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.service.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@ApiOperation("회원 Auth Rest API 컨트롤러")
public class AuthController {
    private final AuthService authService;

    @ApiOperation("회원가입 api - 회원 정보를 입력받아 회원 가입")
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> register(MemberDTO memberDTO) {
        boolean isSuccess = authService.register(memberDTO);

        ResponseDTO responseDTO;

        if (isSuccess) {
            responseDTO = ResponseDTO.builder()
                    .code(200)
                    .status(HttpStatus.OK)
                    .message("회원가입에 성공했습니다.")
                    .build();
        } else {
            responseDTO = ResponseDTO.builder()
                    .code(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .message("회원가입에 실패했습니다.")
                    .build();
        }

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @ApiOperation(value = "로그인 api - 이메일과 패스워드를 입력받아 로그인", notes = "access token과 refresh token이 리턴되며, 클라이언트에 갖고 있습니다.")
    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @ApiOperation(value = "토큰 재발급 api - 토큰이 만료된 경우, Access Token을 재발급 한다. Refresh Token은 동일. 리턴받은 access token을 기존 access token에 바꾸어 저장합니다.", notes = "요청 헤더에 Authorization(access 토큰을 헤더에 추가), REFRESH-TOKEN(refresh 토큰을 헤더에 추가) 필요")
    @PostMapping(value = "/reissue")
    @ApiResponse(code = 401, message = "토큰이 만료되었을때 어느 토큰이 만료되었는지 prefix로 표시.")
    public ResponseEntity<TokenDTO> reissue(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization");
        String refreshToken = httpServletRequest.getHeader("REFRESH-TOKEN");

        return ResponseEntity.ok(authService.reissue(accessToken, refreshToken));
    }

    @ApiOperation(value = "로그아웃 api - 로그아웃 기능", notes = "요청 헤더에 Authorization : access 토큰을 헤더에 추가. 로컬 스토리지에서 access, refresh 토큰 삭제 필요.")
    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("Authorization");

        ResponseDTO responseDTO = authService.logout(accessToken);

        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
} // end class
