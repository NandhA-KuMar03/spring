package com.spring.rest.meetingscheduler.exception;

public class CannotCancelMeetingException extends RuntimeException{
    public CannotCancelMeetingException(String message) {
        super(message);
    }

    public CannotCancelMeetingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCancelMeetingException(Throwable cause) {
        super(cause);
    }
}
