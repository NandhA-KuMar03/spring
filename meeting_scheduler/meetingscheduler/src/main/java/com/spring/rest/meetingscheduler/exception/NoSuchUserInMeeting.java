package com.spring.rest.meetingscheduler.exception;

public class NoSuchUserInMeeting extends RuntimeException{
    public NoSuchUserInMeeting(String message) {
        super(message);
    }

    public NoSuchUserInMeeting(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUserInMeeting(Throwable cause) {
        super(cause);
    }
}
