package com.application.gatekeeper.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.PrematureJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.application.gatekeeper.constants.ErrorConstants.JWT_TOKEN_EXPIRED;
import static com.application.gatekeeper.constants.ErrorConstants.TIME_MISMATCH;

@ControllerAdvice
public class GateKeeperApplicationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(GateKeeperApplicationException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ExpiredJwtException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(JWT_TOKEN_EXPIRED);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(PrematureJwtException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(TIME_MISMATCH);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
