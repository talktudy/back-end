package com.example.talktudy.controller.exception;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void accessDenied() {
        // TODO :
    }

    @GetMapping("/not-secured")
    public void authenticationEntryPoint() {
        // TODO :
    }
} // End class
