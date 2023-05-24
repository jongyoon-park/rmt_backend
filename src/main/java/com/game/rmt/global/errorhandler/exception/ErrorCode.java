package com.game.rmt.global.errorhandler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    BAD_REQUEST_CREATE_GAME(HttpStatus.BAD_REQUEST, "Bad request for created game"),
    BAD_REQUEST_CREATE_PRODUCT(HttpStatus.BAD_REQUEST, "Bad request for created product"),
    BAD_REQUEST_UPDATE_PRODUCT(HttpStatus.BAD_REQUEST, "Bad request for updated product"),
    BAD_REQUEST_ALREADY_UPDATE_PRODUCT(HttpStatus.BAD_REQUEST, "Bad request for already updated product"),
    BAD_REQUEST_CREATE_ACCOUNT(HttpStatus.BAD_REQUEST, "Bad request for created account"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    NOT_FOUND_PLATFORM(HttpStatus.NOT_FOUND, "Not found platform"),
    NOT_FOUND_GAME(HttpStatus.NOT_FOUND, "Not found game"),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "Not found product"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus httpStatus;
    private final String message;
}
