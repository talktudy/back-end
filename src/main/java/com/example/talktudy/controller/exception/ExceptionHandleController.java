package com.example.talktudy.controller.exception;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/exception")
@RestController
@RequiredArgsConstructor
@ApiOperation("Exception 예외 처리 컨트롤리")
public class ExceptionHandleController {

    @GetMapping("/access-denied")
    public ResponseEntity<String> accessDenied() {
        log.error("Access denied exception occurred.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
    }

    @GetMapping("/not-secured")
    public ResponseEntity<String> authenticationEntryPoint() {
        log.error("Authentication entry point exception occurred.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated.");
    }
} // End class
