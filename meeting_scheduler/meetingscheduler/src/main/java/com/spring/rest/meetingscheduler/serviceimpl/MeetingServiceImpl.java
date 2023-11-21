package com.spring.rest.meetingscheduler.serviceimpl;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.exception.AlreadyRoomBookedException;
import com.spring.rest.meetingscheduler.exception.CannotCancelMeetingException;
import com.spring.rest.meetingscheduler.exception.MembersInAnotherMeeting;
import com.spring.rest.meetingscheduler.exception.NoSuchMeetingException;
import com.spring.rest.meetingscheduler.exception.RoomCapacityInsufficientException;
import com.spring.rest.meetingscheduler.repository.MeetingDetailRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRoomRepository;
import com.spring.rest.meetingscheduler.repository.TeamRepository;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {

    private MeetingRoomRepository meetingRoomRepository;
    private MeetingDetailRepository meetingDetailRepository;
    private TeamRepository teamRepository;
    private MeetingRepository meetingRepository;

    @Autowired
    public MeetingServiceImpl(MeetingRoomRepository meetingRoomRepository, MeetingDetailRepository meetingDetailRepository, TeamRepository teamRepository, MeetingRepository meetingRepository) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.meetingDetailRepository = meetingDetailRepository;
        this.teamRepository = teamRepository;
        this.meetingRepository = meetingRepository;
    }

    @Override
    public HashMap<String, Integer> getAvailableRooms(Date date, Time start_time, Time end_time, int count) {
        HashMap<String, Integer> response = new HashMap<>();
        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(date);
        List<Integer> bookedRoomIds = meetingsOnSpecificDate.stream()
                        .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(end_time) && start_time.before(meetingDetail.getMeetingEndTime()))
                        .map(meetingDetail -> meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId())
                        .distinct()
                        .collect(Collectors.toList());
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        meetingRooms.removeIf(meetingRoom -> bookedRoomIds.contains(meetingRoom.getMeetingRoomId()));
        meetingRooms.removeIf(meetingRoom -> meetingRoom.getCapacity() < count);
        meetingRooms.stream()
                .sorted(Comparator.comparing(meetingRoom -> meetingRoom.getCapacity()))
                .forEach(meetingRoom -> response.put(meetingRoom.getRoomName(), meetingRoom.getCapacity()));
        System.out.println(response);
        return response;
    }

    @Override
    public String createMeeting(Date date, Time startTime, Time endTime, int roomId, int teamId, String meetingName) {
        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(date);
        List<MeetingDetail> overlappingMeetings = meetingsOnSpecificDate.stream()
                .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                .distinct()
                .collect(Collectors.toList());
        long numberOfEmployeesAlreadyOccupied = overlappingMeetings.stream()
                .filter(meetingDetail -> meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId() == roomId).count();
        System.out.println(numberOfEmployeesAlreadyOccupied);
        if((int) numberOfEmployeesAlreadyOccupied >0)
            throw new AlreadyRoomBookedException("Room is already booked for the given time");

        List<Integer> employeesOnMeetingDuringRequestTime = overlappingMeetings.stream()
                .map(meetingDetail -> meetingDetail.getEmployee().getEmployeeId())
                .toList();
        System.out.println(employeesOnMeetingDuringRequestTime);

        List<Team> teams = teamRepository.findByTeamId(teamId);
        System.out.println("Teams" + teams);
        List<Integer> employeeIds =  teams.stream()
                .flatMap(team -> team.getEmployees().stream())
                .map(employee -> employee.getEmployeeId())
                .collect(Collectors.toList());
        List<Employee> employees = teams.stream()
                .flatMap(team -> team.getEmployees().stream())
                .collect(Collectors.toList());
        System.out.println(employees);
        boolean ifEmployeesAlreadyBusy = employeeIds.stream()
                .anyMatch(employeesOnMeetingDuringRequestTime::contains);
        if(ifEmployeesAlreadyBusy)
            throw new MembersInAnotherMeeting("Members in some other meeting during this time");

        MeetingRoom meetingRoom = meetingRoomRepository.findByMeetingRoomId(roomId);
        int capacity = meetingRoom.getCapacity();
        long numberOfAttendees = employeeIds.stream().count();
        if(capacity < numberOfAttendees)
            throw new RoomCapacityInsufficientException("Count greater than room capacity try some other room");

        System.out.println("cAN BOOK NOW");
        try {
            Meeting meeting = new Meeting(meetingName, "ACTIVE", (int) numberOfAttendees, meetingRoom);
            meetingRepository.save(meeting);
            employees.stream()
                    .forEach(employee -> {
                        MeetingDetail meetingDetail = new MeetingDetail(meeting, date, startTime, endTime, employee);
                        meetingDetailRepository.save(meetingDetail);
                    });
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return "Saved";
    }

    @Override
    public String cancelMeeting(int meetingId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if(meeting.isEmpty())
            throw new NoSuchMeetingException("No such meeting exists");
        List<MeetingDetail> meetingDetails = meetingDetailRepository.findByMeetingMeetingId(meetingId);
        LocalDate meetingDate = meetingDetails.get(0).getMeetingDate().toLocalDate();
        LocalTime meetingTime = meetingDetails.get(0).getMeetingStartTime().toLocalTime();
        LocalDateTime meetingDateTime = LocalDateTime.of(meetingDate,meetingTime);
        Duration duration = Duration.between(now, meetingDateTime);
        if(duration.isNegative() || duration.toMinutes() < 30)
            throw new CannotCancelMeetingException("Meeting starts in half hour or the meeting is already over cannot cancel");
        meetingDetails.stream()
                .forEach(meetingDetail -> meetingDetailRepository.delete(meetingDetail));
        meetingRepository.delete(meeting.get());
        return "Canceled";
    }
}
