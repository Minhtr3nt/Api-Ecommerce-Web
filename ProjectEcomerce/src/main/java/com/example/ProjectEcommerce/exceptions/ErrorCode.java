package com.example.ProjectEcommerce.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "Username existed", HttpStatus.BAD_REQUEST),
    USER_NOTFOUND(1002, "User not found", HttpStatus.BAD_REQUEST)
    ;
    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    private int code = 1000;
    private String message;
    private HttpStatusCode statusCode;

}
