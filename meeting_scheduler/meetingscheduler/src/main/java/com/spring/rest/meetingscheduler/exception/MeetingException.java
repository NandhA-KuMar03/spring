package com.spring.rest.meetingscheduler.exception;

public class MeetingException extends RuntimeException{
    public MeetingException(String message) {
        super(message);
    }

    public MeetingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingException(Throwable cause) {
        super(cause);
    }
}
