package com.spring.rest.meetingscheduler.exception;

public class AlreadyRoomBookedException extends RuntimeException{
    public AlreadyRoomBookedException(String message) {
        super(message);
    }

    public AlreadyRoomBookedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRoomBookedException(Throwable cause) {
        super(cause);
    }
}
