package com.spring.rest.meetingscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.INVALID_FORMAT;

@ControllerAdvice
public class MeetingExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<MeetingErrorResponse> handleException(MeetingException e){
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<MeetingErrorResponse> handleException(Exception e){
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<MeetingErrorResponse> handleException(MethodArgumentTypeMismatchException e){
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage(INVALID_FORMAT);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
