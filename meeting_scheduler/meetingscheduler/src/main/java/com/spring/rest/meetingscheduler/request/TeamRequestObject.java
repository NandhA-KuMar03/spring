package com.spring.rest.meetingscheduler.request;

import java.util.List;

public class TeamRequestObject {

    private int teamId;
    private List<Integer> employees;

    public TeamRequestObject() {
    }

    public TeamRequestObject(int teamId, List<Integer> employees) {
        this.teamId = teamId;
        this.employees = employees;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public List<Integer> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Integer> employees) {
        this.employees = employees;
    }
}
