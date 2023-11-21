package com.spring.rest.meetingscheduler.exception;

public class MembersInAnotherMeeting extends RuntimeException{
    public MembersInAnotherMeeting(String message) {
        super(message);
    }

    public MembersInAnotherMeeting(String message, Throwable cause) {
        super(message, cause);
    }

    public MembersInAnotherMeeting(Throwable cause) {
        super(cause);
    }
}
