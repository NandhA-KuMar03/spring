package com.spring.rest.meetingscheduler.exception;

public class RoomCapacityInsufficientException extends RuntimeException{
    public RoomCapacityInsufficientException(String message) {
        super(message);
    }

    public RoomCapacityInsufficientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomCapacityInsufficientException(Throwable cause) {
        super(cause);
    }
}
