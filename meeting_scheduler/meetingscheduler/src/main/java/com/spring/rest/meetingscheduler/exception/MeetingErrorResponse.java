package com.spring.rest.meetingscheduler.exception;

public class MeetingErrorResponse {

    private int status;
    private String message;

    public MeetingErrorResponse() {
    }

    public MeetingErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MeetingErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
