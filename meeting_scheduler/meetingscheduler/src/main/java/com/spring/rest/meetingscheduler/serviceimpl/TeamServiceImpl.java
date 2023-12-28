package com.spring.rest.meetingscheduler.serviceimpl;

import com.spring.rest.meetingscheduler.dao.TeamDAO;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.service.TeamService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {


    private TeamDAO teamDAO;

    public TeamServiceImpl(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    @Override
    public List<Team> findAll() {
        return teamDAO.findAll();
    }

    @Override
    public Team findById(int id) {
        return teamDAO.findById(id);
    }

    @Transactional
    @Override
    public Team save(Team team) {
        return teamDAO.save(team);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        teamDAO.deleteById(id);
    }

    @Transactional
    @Override
    public Team addEmployee(List<Integer> employeeIds, int teamId) {
        teamDAO.addEmployee(employeeIds, teamId);
        Team team = teamDAO.findById(teamId);
        return team;
    }
}
