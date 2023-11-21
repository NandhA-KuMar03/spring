package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByTeamId(int teamId);
}
