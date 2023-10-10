package com.example.talktudy.controller.exception;

import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.exception.CustomExpiredJwtException;
import com.example.talktudy.exception.CustomNotAcceptException;
import com.example.talktudy.exception.CustomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFoundException(CustomNotFoundException nfe){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.builder().code(404).status(HttpStatus.NOT_FOUND).message(nfe.getMessage()).build());
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(CustomNotAcceptException.class)
    public ResponseEntity<ResponseDTO> handleNotAcceptException(CustomNotAcceptException nae){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseDTO.builder().code(406).status(HttpStatus.NOT_ACCEPTABLE).message(nae.getMessage()).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException iae){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder().code(400).status(HttpStatus.BAD_REQUEST).message(iae.getMessage()).build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNameNotFoundException(UsernameNotFoundException unfe){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.builder().code(404).status(HttpStatus.NOT_FOUND).message(unfe.getMessage()).build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomExpiredJwtException.class)
    public ResponseEntity<ResponseDTO> handleExpiredJwtException(CustomExpiredJwtException exj) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO.builder().code(401).status(HttpStatus.UNAUTHORIZED).message(exj.getMessage()).build());
    }

} // end class