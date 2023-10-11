package com.example.talktudy.dto.common;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNAUTHORIZED(401 , HttpStatus.UNAUTHORIZED, "Unauthorized Token"),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad Request"),
    INVALID_TOKEN(401 , HttpStatus.UNAUTHORIZED, "Invalid Token"),
    EXPIRED_TOKEN(401, HttpStatus.UNAUTHORIZED, "Expired Token"),
    NULL_VALUE(400, HttpStatus.BAD_REQUEST, "Null Value"),
    NULL_TOKEN_VALUE(400, HttpStatus.BAD_REQUEST, "Token Value is null");

    private int code;
    private HttpStatus httpStatus;
    private String meesage;

    ErrorCode(int code, HttpStatus httpStatus, String meesage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.meesage = meesage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public int getCode() {
        return code;
    }

    public String getMeesage() {
        return meesage;
    }

}
