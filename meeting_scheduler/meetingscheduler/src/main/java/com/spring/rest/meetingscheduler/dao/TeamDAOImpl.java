package com.spring.rest.meetingscheduler.dao;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamDAOImpl implements TeamDAO{

    private EntityManager entityManager;


    @Autowired
    public TeamDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Team> findAll() {
        TypedQuery<Team> teams = entityManager.createQuery("FROM Team", Team.class);
        return teams.getResultList();
    }

    @Override
    public Team findById(int id) {
        Team team = entityManager.find(Team.class, id);
        return team;
    }

    @Override
    public Team save(Team team) {
        Team team1 = entityManager.merge(team);
        return team1;
    }

    @Override
    public void deleteById(int id) {
        Team team = entityManager.find(Team.class, id);
        entityManager.remove(team);
    }

    @Override
    public String addEmployee(List<Integer> employeeIds, int teamId) {
        Team team = entityManager.find(Team.class, teamId);
        for(Integer employeeId : employeeIds){
            Employee employee = entityManager.find(Employee.class, employeeId);
            team.addEmployee(employee);
        }
        return "Added";
    }
}
