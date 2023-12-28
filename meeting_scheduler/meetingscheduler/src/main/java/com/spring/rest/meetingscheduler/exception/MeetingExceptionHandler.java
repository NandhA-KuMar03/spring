package com.spring.rest.meetingscheduler.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;

import static com.spring.rest.meetingscheduler.constants.CommonConstants.DUPLICATE_VALUES_NOT_ALLOWED;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.INVALID_FORMAT;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.NO_SUCH_TEAM;

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

    @ExceptionHandler
    public ResponseEntity<MeetingErrorResponse> handleException(DateTimeParseException e){
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage(INVALID_FORMAT);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<MeetingErrorResponse> handleException(NullPointerException e){
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage(NO_SUCH_TEAM);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MeetingErrorResponse> handleException(DataIntegrityViolationException e){
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage(DUPLICATE_VALUES_NOT_ALLOWED);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
