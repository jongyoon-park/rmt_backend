package com.game.rmt.global.errorhandler.handler;


import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import com.game.rmt.global.errorhandler.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return handleCustomExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> handleCustomExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode));
    }

}
