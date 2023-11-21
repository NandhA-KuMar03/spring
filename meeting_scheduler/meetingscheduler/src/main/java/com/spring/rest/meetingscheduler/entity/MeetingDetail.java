package com.spring.rest.meetingscheduler.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "meeting_detail")
public class MeetingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_detail_id")
    private int meetingDetailId;

    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Column(name = "meeting_date")
    private Date meetingDate;
    @Column(name = "meeting_start_time")
    private Time meetingStartTime;

    @Column(name = "meeting_end_time")
    private Time meetingEndTime;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public MeetingDetail() {
    }

    public MeetingDetail(Meeting meeting, Date meetingDate, Time meetingStartTime, Time meetingEndTime, Employee employee) {
        this.meeting = meeting;
        this.meetingDate = meetingDate;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.employee = employee;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Time getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(Time meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public Time getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(Time meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "MeetingDetail{" +
                "meeting=" + meeting +
                ", meetingDate=" + meetingDate +
                ", meetingStartTime=" + meetingStartTime +
                ", meetingEndTime=" + meetingEndTime +
                ", employee=" + employee +
                '}';
    }
}
