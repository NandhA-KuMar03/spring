package com.spring.rest.meetingscheduler.exception;

public class NoSuchMeetingException extends RuntimeException{
    public NoSuchMeetingException(String message) {
        super(message);
    }

    public NoSuchMeetingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMeetingException(Throwable cause) {
        super(cause);
    }
}
