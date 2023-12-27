package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.exception.MeetingErrorResponse;
import com.spring.rest.meetingscheduler.repository.EmployeeRepository;
import com.spring.rest.meetingscheduler.repository.MeetingDetailRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRoomRepository;
import com.spring.rest.meetingscheduler.repository.TeamRepository;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.serviceimpl.MeetingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    void availableRooms() {
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        Meeting meeting2 = new Meeting("Mentor Meeting", "ACTIVE", 4, meetingRoom2);
        Date date = new Date(2023-11-28);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-28")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingDetails.add(meetingDetail1);
        meetingRooms.add(meetingRoom2);
        meetingRooms.add(meetingRoom1);
        meetingRooms.add(meetingRoom3);
        MeetingRequestObject meetingDetail2 = new MeetingRequestObject();
        meetingDetail2.setMeetingDate("2023-11-28");
        meetingDetail2.setMeetingStartTime("11:00:00");
        meetingDetail2.setMeetingEndTime("11:30:00");
        when(meetingDetailRepository.findByMeetingDate(Date.valueOf(LocalDate.parse("2023-11-28")))).thenReturn(meetingDetails);
        when(meetingRoomRepository.findAll()).thenReturn(meetingRooms);
        List<MeetingRoom> response = meetingService.getAvailableRooms(meetingDetail2);
        assertEquals(meetingRooms, response);
    }

    @Test
    void createMeeting() {
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Daily Scrum", "ACTIVE", 2, meetingRoom2);
        Meeting meeting2 = new Meeting("Mentor Meeting", "ACTIVE", 2, meetingRoom2);
        meeting2.setMeetingId(0);
        Date date = Date.valueOf("2023-11-28");
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-23")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
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
        MeetingRequestObject meetingDetail2 = new MeetingRequestObject();
        meetingDetail2.setMeetingDate("2023-11-28");
        meetingDetail2.setMeetingStartTime("23:00:00");
        meetingDetail2.setMeetingEndTime("23:30:00");
        meetingDetail2.setMeetingName("Mentor Meeting");
        meetingDetail2.setRoomId(2);
        meetingDetail2.setTeamId(1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetails);
        when(teamRepository.findByTeamId(1)).thenReturn(team1);
        when(meetingRoomRepository.findByMeetingRoomId(2)).thenReturn(meetingRoom2);
        when(meetingRepository.save(meeting2)).thenReturn(meeting2);
        MeetingResponse response = meetingService.createMeeting(meetingDetail2);
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setMeetingDate(Date.valueOf("2023-11-28"));
        meetingResponse.setMeetingName("Daily Scrum");
        meetingResponse.setMeetingStartTime(Time.valueOf("23:00:00"));
        meetingResponse.setMeetingEndTime(Time.valueOf("23:30:00"));
        assertEquals(meetingResponse, response);
    }


    @Test
    void createMeetingAlreadyBookedTest() {
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setStatus(HttpStatus.OK.value());
        errorResponse.setMessage("Room is already booked for the given time");
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        Team team1 = new Team("SE");
        team1.setTeamId(1);
        team1.setEmployees(employees);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        Date date = Date.valueOf("2023-12-28");
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-12-28")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        meetingDetails.add(meetingDetail1);
        MeetingRequestObject meetingDetail2 = new MeetingRequestObject();
        meetingDetail2.setMeetingDate("2023-12-28");
        meetingDetail2.setMeetingStartTime("17:30:00");
        meetingDetail2.setMeetingEndTime("18:30:00");
        meetingDetail2.setMeetingName("Smiles");
        meetingDetail2.setTeamId(1);
        meetingDetail2.setRoomId(1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetails);
        try {
            meetingService.createMeeting(meetingDetail2);
        } catch (Exception e) {
            assertEquals(errorResponse.getMessage(), e.getMessage());
        }
    }

    @Test
    void createMeetingMembersInAnotherMeeting() {
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setStatus(HttpStatus.OK.value());
        String expectedResponse = "Members in some other meeting during this time";
        errorResponse.setMessage(expectedResponse);
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        MeetingRoom meetingRoom2 = new MeetingRoom("Tanjore", 20);
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 35);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        Team team1 = new Team("SE");
        team1.setTeamId(1);
        team1.setEmployees(employees);
        Team team2 = new Team("IP");
        team2.addEmployee(employee2);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        Date date = Date.valueOf("2023-12-28");
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-12-28")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        meetingDetails.add(meetingDetail1);
        MeetingRequestObject meetingDetail2 = new MeetingRequestObject();
        String date1 = "2023-12-28";
        String startTime = "17:30:00";
        String endTime = "18:30:00";
        meetingDetail2.setMeetingDate(date1);
        meetingDetail2.setMeetingStartTime(startTime);
        meetingDetail2.setMeetingEndTime(endTime);
        meetingDetail2.setMeetingName("Solo");
        meetingDetail2.setRoomId(2);
        meetingDetail2.setTeamId(2);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetails);
        when(teamRepository.findByTeamId(2)).thenReturn(team2);
        try {
            meetingService.createMeeting(meetingDetail2);
        } catch (Exception e) {
            assertEquals(errorResponse.getMessage(), e.getMessage());
        }
    }

    @Test
    void cancel() {
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setRoomId(1);
        meetingResponse.setMeetingId(1);
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        employee1.setEmployeeId(1);
        employee2.setEmployeeId(2);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        meetingRoom1.setMeetingRoomId(1);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        meeting1.setMeetingId(1);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-12-31")), Time.valueOf(LocalTime.parse("23:00:00")), Time.valueOf(LocalTime.parse("23:01:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-12-31")), Time.valueOf(LocalTime.parse("23:00:00")), Time.valueOf(LocalTime.parse("23:01:00")), employee1);
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        meetingDetails.add(meetingDetail2);
        meetingDetails.add(meetingDetail1);
        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails);
        MeetingResponse actual = meetingService.cancelMeeting(1);
        assertEquals(meetingResponse, actual);
    }


    @Test
    void cancelMeetingOverExceptionTest() {
        MeetingErrorResponse errorResponse = new MeetingErrorResponse();
        errorResponse.setMessage("Meeting starts in half hour or the meeting is already over cannot cancel");
        Employee employee1 = new Employee("Nandha", "nandha@cdw.com");
        Employee employee2 = new Employee("Naveen", "naveen@cdw.com");
        employee1.setEmployeeId(1);
        employee2.setEmployeeId(2);
        MeetingRoom meetingRoom1 = new MeetingRoom("London", 23);
        meetingRoom1.setMeetingRoomId(1);
        Meeting meeting1 = new Meeting("Daily scrum", "ACTIVE", 3, meetingRoom1);
        meeting1.setMeetingId(1);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-23")), Time.valueOf(LocalTime.parse("23:00:00")), Time.valueOf(LocalTime.parse("23:01:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-23")), Time.valueOf(LocalTime.parse("23:00:00")), Time.valueOf(LocalTime.parse("23:01:00")), employee1);
        List<MeetingDetail> meetingDetails = new ArrayList<>();
        meetingDetails.add(meetingDetail2);
        meetingDetails.add(meetingDetail1);
        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails);
        try {
            MeetingResponse response = meetingService.cancelMeeting(1);
        } catch (Exception e) {
            assertEquals(errorResponse.getMessage(), e.getMessage());
        }
    }

    @Test
    void updateTiming() {
        MeetingResponse expectResponse = new MeetingResponse();
        expectResponse.setMeetingId(1);
        expectResponse.setMeetingName("Scrum");
        expectResponse.setMeetingDate(Date.valueOf("2023-12-28"));
        expectResponse.setMeetingStartTime(Time.valueOf("18:40:00"));
        expectResponse.setMeetingEndTime(Time.valueOf("23:30:00"));
        expectResponse.setRoomName("London");
        expectResponse.setRoomId(1);
        List<Long> emp = Arrays.asList(1L,2L);
        expectResponse.setEmployeeId(emp);
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
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee3);
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
        Date date = Date.valueOf("2023-12-28");
        Time startTime = Time.valueOf(LocalTime.parse("18:40:00"));
        Time endTime = Time.valueOf(LocalTime.parse("23:30:00"));

        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        MeetingResponse response = meetingService.updateMeeting(1, date, "Scrum",  startTime , endTime);
        assertEquals(expectResponse, response);
    }

    @Test
    void updateTimingNoMeetingExists() {
        String expectResponse = "Give a valid meeting Id";
        try {
            meetingService.updateMeeting(1, new Date(0), "Scrum", new Time(0), new Time(0));
        } catch (Exception e) {
            assertEquals(expectResponse, e.getMessage());
        }
    }

        @Test
    void updateTimingMismatch() {
        String expectResponse = "Start time is after end time";
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
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-12-28")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        List<MeetingDetail> meetingDetails1 = new ArrayList<>();
        meetingDetails1.add(meetingDetail1);
        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(Date.valueOf(LocalDate.parse("2023-12-28")))).thenReturn(meetingDetails1);
        try {
            meetingService.updateMeeting(1, null, "", Time.valueOf(LocalTime.parse("19:30:00")), new Time(0));
        } catch (Exception e) {
            assertEquals(expectResponse, e.getMessage());
        }
    }


    @Test
    void updateRoom() {
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setRoomId(1);
        meetingResponse.setMeetingId(1);
        meetingResponse.setRoomName("London");
        List<Long> emp = Arrays.asList(1L,2L);
        meetingResponse.setEmployeeId(emp);
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
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee3);
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

        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        when(meetingRoomRepository.findByMeetingRoomId(1)).thenReturn(meetingRoom1);
        MeetingResponse response = meetingService.updateRoom(1, 1);
        assertEquals(meetingResponse, response);
    }

    @Test
    void updateRoomOccupied() {
        String expectResponse = "The room is already occupied. Try some other rooms";
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
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("11:20:00")), Time.valueOf(LocalTime.parse("12:20:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("11:20:00")), Time.valueOf(LocalTime.parse("12:20:00")), employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee3);
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
        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        try {
            meetingService.updateRoom(1, 2);
        } catch (Exception e) {
            assertEquals(expectResponse, e.getMessage());
        }
    }

    @Test
    void updateRoomCapacityException() {
        String expectResponse = "This room has lower capacity find some other room";
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
        MeetingRoom meetingRoom3 = new MeetingRoom("Training Room 1", 1);
        meetingRoom1.setMeetingRoomId(1);
        meetingRoom2.setMeetingRoomId(2);
        meetingRoom3.setMeetingRoomId(3);
        Meeting meeting1 = new Meeting("Leo scrum", "Active", 2, meetingRoom1);
        meeting1.setMeetingId(1);
        Meeting meeting2 = new Meeting("mentors meeting", "Active", 2, meetingRoom2);
        meeting2.setMeetingId(2);
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("11:20:00")), Time.valueOf(LocalTime.parse("12:20:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("11:20:00")), Time.valueOf(LocalTime.parse("12:20:00")), employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee3);
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
        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        when(meetingRoomRepository.findByMeetingRoomId(3)).thenReturn(meetingRoom3);
        try {
            meetingService.updateRoom(1, 3);
        } catch (Exception e) {
            assertEquals(expectResponse, e.getMessage());
        }
    }


    @Test
    void changePeople() {
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setRoomId(1);
        meetingResponse.setMeetingId(1);
        meetingResponse.setRoomName("London");
        List<Long> emp = Arrays.asList(1L,2L);
        meetingResponse.setEmployeeId(emp);
        meetingResponse.setMeetingName("Leo scrum");
        meetingResponse.setMeetingDate(Date.valueOf("2023-11-22"));
        meetingResponse.setMeetingStartTime(Time.valueOf("18:00:00"));
        meetingResponse.setMeetingEndTime(Time.valueOf("19:00:00"));
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
        MeetingDetail meetingDetail1 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee1);
        MeetingDetail meetingDetail2 = new MeetingDetail(meeting1, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("18:00:00")), Time.valueOf(LocalTime.parse("19:00:00")), employee2);
        MeetingDetail meetingDetail3 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee2);
        MeetingDetail meetingDetail4 = new MeetingDetail(meeting2, Date.valueOf(LocalDate.parse("2023-11-22")), Time.valueOf(LocalTime.parse("12:00:00")), Time.valueOf(LocalTime.parse("13:00:00")), employee3);
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

        when(meetingRepository.findByMeetingId(1)).thenReturn(Optional.of(meeting1));
        when(meetingDetailRepository.findByEmployeeEmployeeIdAndMeetingMeetingId(2, 1)).thenReturn(meetingDetail2);
        when(meetingDetailRepository.findByMeetingMeetingId(1)).thenReturn(meetingDetails1);
        when(meetingDetailRepository.findByMeetingDate(date)).thenReturn(meetingDetailsAll);
        when(employeeRepository.findByEmployeeId(3)).thenReturn(employee3);
        MeetingResponse response = meetingService.updatePeople(1, addPeople, removePeople);
        assertEquals(response, meetingResponse);
    }

}