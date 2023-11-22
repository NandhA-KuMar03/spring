package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.repository.EmployeeRepository;
import com.spring.rest.meetingscheduler.repository.MeetingDetailRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRoomRepository;
import com.spring.rest.meetingscheduler.repository.TeamRepository;
import com.spring.rest.meetingscheduler.serviceimpl.MeetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

    @InjectMocks
    MeetingServiceImpl meetingService;
    @Mock
    MeetingRoomRepository meetingRoomRepository;
    @Mock
    TeamRepository teamRepository;
    @Mock
    MeetingDetailRepository meetingDetailRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    MeetingRepository meetingRepository;


    @Test
    void availableRooms(){
        HashMap<String, Integer> expectedResponse = new HashMap<>();
        expectedResponse.put("2 " + "Tanjore", 20);
        expectedResponse.put("3 "+"Training Room 1", 35);
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        Meeting meeting2 = new Meeting("Mentor Meeting", "ACTIVE", 4, meetingRoom2);
        Date date = new Date(2023-11-23);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee1);
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingDetails.add(meetingDetail1);
        meetingRooms.add(meetingRoom1);
        meetingRooms.add(meetingRoom2);
        meetingRooms.add(meetingRoom3);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetails);
        when(meetingRoomRepository.findAll()).thenReturn(meetingRooms);
        HashMap<String, Integer> response = meetingService.getAvailableRooms(date, Time.valueOf(LocalTime.parse("17:30:00")), Time.valueOf(LocalTime.parse("18:30:00")), 4);
        assertEquals(expectedResponse, response);
    }

    @Test
    void createMeeting(){
        String expectedResponse = "Saved";
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        Meeting meeting2 = new Meeting("Mentor Meeting", "ACTIVE", 4, meetingRoom2);
        Date date = new Date(2023-11-23);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee1);
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingDetails.add(meetingDetail1);
        meetingRooms.add(meetingRoom1);
        meetingRooms.add(meetingRoom2);
        meetingRooms.add(meetingRoom3);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        Team team1 = new Team("SE");
        team1.setTeamId(1);
        team1.setEmployees(employees);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetails);
        when(teamRepository.findByTeamId(1)).thenReturn(team1);
        when(meetingRoomRepository.findByMeetingRoomId(2)).thenReturn(meetingRoom2);
        String response = meetingService.createMeeting(date,Time.valueOf(LocalTime.parse("16:30:00")), Time.valueOf(LocalTime.parse("17:30:00")), 2, 1 , "Scrum" );
        assertEquals(expectedResponse, response);
    }


    @Test
    void cancel(){
        String expectedResponse = "Canceled";
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        employee1.setEmployeeId(1);
        employee2.setEmployeeId(2);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        meetingRoom1.setMeetingRoomId(1);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        meeting1.setMeetingId(1);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("23:00:00")), Time.valueOf(LocalTime.parse("23:01:00")),employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("23:00:00")), Time.valueOf(LocalTime.parse("23:01:00")),employee1);
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        meetingDetails.add(meetingDetail2);
        meetingDetails.add(meetingDetail1);
        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails);
        String response = meetingService.cancelMeeting(1);
        assertEquals(expectedResponse, response);
    }

    @Test
    void updateTiming(){
        String expectResponse = "Updated";
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        employee1.setEmployeeId(1);
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        employee2.setEmployeeId(2);
        Employee employee3 = new Employee("Yagzan", "yagzan@cdw.com");
        employee3.setEmployeeId(3);
        List<Employee> team1emp = new ArrayList<>();
        List<Employee> team2emp = new ArrayList<>();
        team1emp.add(employee1);
        team1emp.add(employee2);
        team2emp.add(employee2);
        team2emp.add(employee3);
        Team team1 = new Team("Leo");
        team1.setTeamId(1);
        team1.setEmployees(team1emp);
        Team team2 = new Team("Mentors");
        team2.setTeamId(2);
        team2.setEmployees(team2emp);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Leo scrum", "Active", 2, meetingRoom1);
        meeting1.setMeetingId(1);
        Meeting meeting2 = new Meeting("mentors meeting", "Active", 2, meetingRoom2);
        meeting2.setMeetingId(2);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")),employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")),employee3);
        List<MeetingDetail> meetingDetails1 = new ArrayList<>();
        meetingDetails1.add(meetingDetail1);
        meetingDetails1.add(meetingDetail2);
        List<MeetingDetail> meetingDetails2 = new ArrayList<>();
        meetingDetails2.add(meetingDetail3);
        meetingDetails2.add(meetingDetail4);
        List<MeetingDetail> meetingDetailsAll = new ArrayList<>();
        meetingDetailsAll.add(meetingDetail1);
        meetingDetailsAll.add(meetingDetail2);
        meetingDetailsAll.add(meetingDetail3);
        meetingDetailsAll.add(meetingDetail4);
        Date date = new Date(2023-11-23);
        Time startTime = Time.valueOf(LocalTime.parse("18:40:00"));
        Time endTime = Time.valueOf(LocalTime.parse("23:30:00"));

        when(meetingRepository.findById(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        String response = meetingService.updateMeeting(1, Optional.of(date), Optional.of("Scrum"), Optional.of(startTime), Optional.empty());
        assertEquals(expectResponse, response);
    }

    @Test
    void updateRoom(){
        String expectResponse = "Room changed";
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        employee1.setEmployeeId(1);
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        employee2.setEmployeeId(2);
        Employee employee3 = new Employee("Yagzan", "yagzan@cdw.com");
        employee3.setEmployeeId(3);
        List<Employee> team1emp = new ArrayList<>();
        List<Employee> team2emp = new ArrayList<>();
        team1emp.add(employee1);
        team1emp.add(employee2);
        team2emp.add(employee2);
        team2emp.add(employee3);
        Team team1 = new Team("Leo");
        team1.setTeamId(1);
        team1.setEmployees(team1emp);
        Team team2 = new Team("Mentors");
        team2.setTeamId(2);
        team2.setEmployees(team2emp);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Leo scrum", "Active", 2, meetingRoom1);
        meeting1.setMeetingId(1);
        Meeting meeting2 = new Meeting("mentors meeting", "Active", 2, meetingRoom2);
        meeting2.setMeetingId(2);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")),employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")),employee3);
        List<MeetingDetail> meetingDetails1 = new ArrayList<>();
        meetingDetails1.add(meetingDetail1);
        meetingDetails1.add(meetingDetail2);
        List<MeetingDetail> meetingDetails2 = new ArrayList<>();
        meetingDetails2.add(meetingDetail3);
        meetingDetails2.add(meetingDetail4);
        List<MeetingDetail> meetingDetailsAll = new ArrayList<>();
        meetingDetailsAll.add(meetingDetail1);
        meetingDetailsAll.add(meetingDetail2);
        meetingDetailsAll.add(meetingDetail3);
        meetingDetailsAll.add(meetingDetail4);
        Date date = Date.valueOf(LocalDate.parse("2023-11-22"));

        when(meetingRepository.findById(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        when(meetingRoomRepository.findByMeetingRoomId(1)).thenReturn(meetingRoom1);
        String response = meetingService.updateRoom(1,1);
        assertEquals(expectResponse,response);
    }

    @Test
    void changePeople(){
        String expectResponse = "People updated";
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        employee1.setEmployeeId(1);
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        employee2.setEmployeeId(2);
        Employee employee3 = new Employee("Yagzan", "yagzan@cdw.com");
        employee3.setEmployeeId(3);
        List<Employee> team1emp = new ArrayList<>();
        List<Employee> team2emp = new ArrayList<>();
        team1emp.add(employee1);
        team1emp.add(employee2);
        team2emp.add(employee2);
        team2emp.add(employee3);
        Team team1 = new Team("Leo");
        team1.setTeamId(1);
        team1.setEmployees(team1emp);
        Team team2 = new Team("Mentors");
        team2.setTeamId(2);
        team2.setEmployees(team2emp);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Leo scrum", "Active", 2, meetingRoom1);
        meeting1.setMeetingId(1);
        Meeting meeting2 = new Meeting("mentors meeting", "Active", 2, meetingRoom2);
        meeting2.setMeetingId(2);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")),employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2,Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")),employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")),employee3);
        List<MeetingDetail> meetingDetails1 = new ArrayList<>();
        meetingDetails1.add(meetingDetail1);
        meetingDetails1.add(meetingDetail2);
        List<MeetingDetail> meetingDetails2 = new ArrayList<>();
        meetingDetails2.add(meetingDetail3);
        meetingDetails2.add(meetingDetail4);
        List<MeetingDetail> meetingDetailsAll = new ArrayList<>();
        meetingDetailsAll.add(meetingDetail1);
        meetingDetailsAll.add(meetingDetail2);
        meetingDetailsAll.add(meetingDetail3);
        meetingDetailsAll.add(meetingDetail4);
        Date date = Date.valueOf(LocalDate.parse("2023-11-22"));
        List<Integer> addPeople = new ArrayList<>();
        addPeople.add(3);
        List<Integer> removePeople = new ArrayList<>();
        removePeople.add(2);

        when(meetingRepository.findById(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByEmployeeEmployeeIdAndMeetingMeetingId(2,1)).thenReturn(meetingDetail2);
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        when(employeeRepository.findByEmployeeId(3)).thenReturn(employee3);
        String response = meetingService.updatePeople(1, Optional.of(addPeople), Optional.of(removePeople));
    }
}
