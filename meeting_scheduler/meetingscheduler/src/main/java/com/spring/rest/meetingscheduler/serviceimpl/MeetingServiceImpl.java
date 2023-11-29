package com.spring.rest.meetingscheduler.serviceimpl;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.exception.MeetingException;
import com.spring.rest.meetingscheduler.repository.EmployeeRepository;
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
import java.util.stream.Stream;


@Service
public class MeetingServiceImpl implements MeetingService {

    private MeetingRoomRepository meetingRoomRepository;
    private MeetingDetailRepository meetingDetailRepository;
    private TeamRepository teamRepository;
    private MeetingRepository meetingRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public MeetingServiceImpl(MeetingRoomRepository meetingRoomRepository, MeetingDetailRepository meetingDetailRepository, TeamRepository teamRepository, MeetingRepository meetingRepository, EmployeeRepository employeeRepository) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.meetingDetailRepository = meetingDetailRepository;
        this.teamRepository = teamRepository;
        this.meetingRepository = meetingRepository;
        this.employeeRepository = employeeRepository;
    }

/*  Get available rooms service
    Params: Date of the meeting, start time, end time, number of attendees
    Returns List of available rooms and capacity
        Get all the meetings happening at the given date
        Check if any meetings overlap during the given timings
        Get all rooms other than overlapping meetings & only rooms which have capacity greater than number of attendees
*/
    @Override
    public HashMap<String, Integer> getAvailableRooms(MeetingDetail meetingDetails, int count) {
        Date date = meetingDetails.getMeetingDate();
        Time startTime = meetingDetails.getMeetingStartTime();
        Time endTime = meetingDetails.getMeetingEndTime();
        HashMap<String, Integer> response = new HashMap<>();
        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(date);
        List<Integer> bookedRoomIds = meetingsOnSpecificDate.stream()
                        .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                        .map(meetingDetail -> meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId())
                        .distinct()
                        .collect(Collectors.toList());
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        meetingRooms.removeIf(meetingRoom -> bookedRoomIds.contains(meetingRoom.getMeetingRoomId()));
        meetingRooms.removeIf(meetingRoom -> meetingRoom.getCapacity() < count);
        meetingRooms.stream()
                .sorted(Comparator.comparing(meetingRoom -> meetingRoom.getCapacity()))
                .forEach(meetingRoom -> response.put(meetingRoom.getMeetingRoomId() + " " +meetingRoom.getRoomName(), meetingRoom.getCapacity()));
        return response;
    }

/*
    Create Meeting Service
    Params: Date of the meeting, start time, end time, room Id to be booked, Team Id, Meeting name
    Returns Successful or Fail
        Get all the meetings happening at the given date
        Check if any meetings overlap during the given timings
        Check if overlapping meeting room and the given room does not match
        Get all the employees from the overlapping meeting
        Check if any employee from the team is not already in some other meeting
        Check capacity of the room to teams count
        Create meeting
*/
    @Override
    public String createMeeting(MeetingDetail meetingDetails, int roomId, int teamId, String meetingName) {
        Date date = meetingDetails.getMeetingDate();
        Time startTime = meetingDetails.getMeetingStartTime();
        Time endTime = meetingDetails.getMeetingEndTime();
        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(date);
        List<MeetingDetail> overlappingMeetings = meetingsOnSpecificDate.stream()
                .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                .distinct()
                .collect(Collectors.toList());
        long numberOfEmployeesAlreadyOccupied = overlappingMeetings.stream()
                .filter(meetingDetail -> meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId() == roomId).count();
        if((int) numberOfEmployeesAlreadyOccupied >0)
            throw new MeetingException("Room is already booked for the given time");

        List<Long> employeesOnMeetingDuringRequestTime = overlappingMeetings.stream()
                .map(meetingDetail -> meetingDetail.getEmployee().getEmployeeId())
                .toList();

        Team teams = teamRepository.findByTeamId(teamId);
        List<Employee> employees = teams.getEmployees();
        List<Long> employeeIds = employees.stream()
                .map(employee -> employee.getEmployeeId())
                .collect(Collectors.toList());
        boolean ifEmployeesAlreadyBusy = employeeIds.stream()
                .anyMatch(employeesOnMeetingDuringRequestTime::contains);
        if(ifEmployeesAlreadyBusy)
            throw new MeetingException("Members in some other meeting during this time");

        MeetingRoom meetingRoom = meetingRoomRepository.findByMeetingRoomId(roomId);
        int capacity = meetingRoom.getCapacity();
        long numberOfAttendees = employeeIds.stream().count();
        if(capacity < numberOfAttendees)
            throw new MeetingException("Count greater than room capacity try some other room");

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

/*
    Cancel Meeting service
    Params: Meeting Id
    Returns Canceled or not
        Check if meeting exists
        Check if meeting is going to start in less than half an hour
        Delete Meeting
*/
    @Override
    public String cancelMeeting(int meetingId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if(meeting.isEmpty())
            throw new MeetingException("No such meeting exists");
        List<MeetingDetail> meetingDetails = meetingDetailRepository.findByMeetingMeetingId(meetingId);
        LocalDate meetingDate = meetingDetails.get(0).getMeetingDate().toLocalDate();
        LocalTime meetingTime = meetingDetails.get(0).getMeetingStartTime().toLocalTime();
        LocalDateTime meetingDateTime = LocalDateTime.of(meetingDate,meetingTime);
        Duration duration = Duration.between(now, meetingDateTime);
        if(duration.isNegative() || duration.toMinutes() < 30)
            throw new MeetingException("Meeting starts in half hour or the meeting is already over cannot cancel");
        meetingDetails.stream()
                .forEach(meetingDetail -> meetingDetailRepository.delete(meetingDetail));
        meetingRepository.delete(meeting.get());
        return "Canceled";
    }


/*
    Update Meeting Service - updates date, meeting name, start time , end time
    Params: meetingId, date, meeting name, start time, end Time
    Returns updated or not
        Check if the meeting exists
        Get old meeting start time and meeting end time
        Change meeting name
        to change meeting date or meeting timings
            use old values or use new values if present
            get all the meeting happening at given date and timing
            check if any overlapping meeting happens due to room
            get all the employees from the overlapping meetings
            check if employees from the overlapping meetings do not match with employees from current meeting
            Update meeting
 */
    @Override
    public String updateMeeting(int meetingId, Optional<Date> date, Optional<String> meetingName, Optional<Time> startTime, Optional<Time> endTime) {
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);

        if(meeting.isEmpty())
            throw new MeetingException("Give a valid meeting Id");
        List<MeetingDetail> meetingDetailEntity = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId());
        Optional<MeetingDetail> meetingDetail1 = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId()).stream().findFirst();
        Time newStartTime = meetingDetail1.get().getMeetingStartTime();
        Time newEndTime = meetingDetail1.get().getMeetingEndTime();
        Date newDate = meetingDetail1.get().getMeetingDate();

        if(meetingName.isPresent())
            meeting.get().setMeetingName(meetingName.get());

        if(date.isPresent() || startTime.isPresent() || endTime.isPresent()){
            if(date.isPresent()){
                newDate = date.get();
            }
            if(startTime.isPresent()){
                newStartTime = startTime.get();
            }
            if(endTime.isPresent()){
                newEndTime = endTime.get();
            }
            List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(newDate);
            Time finalNewEndTime = newEndTime;
            Time finalNewStartTime = newStartTime;
            Date finalNewDate = newDate;
            if(finalNewStartTime.after(finalNewEndTime))
                throw new MeetingException("start time is after end time");
            List<MeetingDetail> overlappingMeetings = meetingsOnSpecificDate.stream()
                    .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(finalNewEndTime) && finalNewStartTime.before(meetingDetail.getMeetingEndTime()))
                    .filter(overlappingMeeting -> (overlappingMeeting.getMeeting().getMeetingId() != meetingId))
                    .distinct()
                    .collect(Collectors.toList());
            long n = overlappingMeetings.stream()
                    .filter(overlappingMeeting-> overlappingMeeting.getMeeting().getMeetingRoom().getMeetingRoomId() == meetingDetail1.get().getMeeting().getMeetingRoom().getMeetingRoomId()).count();

            List<Long> employeesOnMeetingDuringRequestTime = overlappingMeetings.stream()
                    .map(meetingDetail -> meetingDetail.getEmployee().getEmployeeId())
                    .toList();

            List<Long> employeesInCurrentMeeting = meetingDetailEntity.stream()
                    .map(meetingDetail ->  meetingDetail.getEmployee().getEmployeeId())
                    .collect(Collectors.toList());
            boolean ifEmployeesAlreadyBusy = employeesInCurrentMeeting.stream()
                    .anyMatch(employeesOnMeetingDuringRequestTime::contains);
            if(ifEmployeesAlreadyBusy)
                throw new MeetingException("Employees in your meeting are not available for given date and time");
            if((int) n > 0)
                throw new MeetingException("Some other meeting is already scheduled for given date and time");

            meetingDetailEntity.stream()
                    .forEach(meetingDetail ->
                    {
                        meetingDetail.setMeetingDate(finalNewDate);
                        meetingDetail.setMeetingStartTime(finalNewStartTime);
                        meetingDetail.setMeetingEndTime(finalNewEndTime);
                        meetingDetailRepository.save(meetingDetail);
                    });
        }
        meetingRepository.save(meeting.get());
        return "Updated";
    }

/*
    Update room service
    params: meeting Id, room Id
    return Room changed or not
        Check if meeting exists
        check if the room is available for given date and time
        Check capacity
 */
    @Override
    public String updateRoom(int meetingId, int roomId) {
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if(meeting.isEmpty())
            throw new MeetingException("Give a valid meeting Id");
        MeetingDetail meetingDetailEntity = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId()).get(0);
        Time startTime = meetingDetailEntity.getMeetingStartTime();
        Time endTime = meetingDetailEntity.getMeetingEndTime();
        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(meetingDetailEntity.getMeetingDate());
        List<MeetingDetail> overlappingMeetings = meetingsOnSpecificDate.stream()
                .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                .filter(overlappingMeeting -> (overlappingMeeting.getMeeting().getMeetingId() != meetingId))
                .distinct()
                .collect(Collectors.toList());
        long n = overlappingMeetings.stream()
                .filter(overlappingMeeting -> (overlappingMeeting.getMeeting().getMeetingRoom().getMeetingRoomId() == roomId)).count();
        if(n>0)
            throw new MeetingException("The room is already occupied. Try some other rooms");


            MeetingRoom meetingRoom = meetingRoomRepository.findByMeetingRoomId(roomId);
            if(meetingRoom.getCapacity() < meeting.get().getCount())
                throw new MeetingException("This room has lower capacity find some other room");
            try{
                meeting.get().setMeetingRoom(meetingRoom);
                meetingRepository.save(meeting.get());
                return "Room changed";
            }
        catch (Exception e){
            throw new RuntimeException("Enter a correct room id");
        }
    }

/*
    update People service - add and remove employees from Meeting
    Param meeting id, list of people to be added, list of people to be removed
    returns updated or not
    check if meeting exists
    to remove people
        check if employees exist and present in meeting and remove
    to add people
        check if employees are available at the given time and date and add employees to meeting
 */
    @Override
    public String updatePeople(int meetingId, Optional<List<Integer>> addPeople, Optional<List<Integer>> removePeople) {
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if (meeting.isEmpty())
            throw new MeetingException("Give a valid meeting Id");

        if (removePeople.isPresent()) {
            try {
                Stream<Integer> removeEmployees = removePeople.map(List::stream).orElse(Stream.empty());
                removeEmployees.map(MeetingDetail -> meetingDetailRepository.findByEmployeeEmployeeIdAndMeetingMeetingId(MeetingDetail, meetingId))
                        .forEach(meetingDetail -> meetingDetailRepository.delete(meetingDetail));
            }
            catch (Exception e) {
                throw new MeetingException("Employee is not in meeting");
            }
            meeting.get().setCount((int) (meeting.get().getCount() - removePeople.stream().count()));
        }

        if(addPeople.isPresent()) {
            Stream<Integer> addEmployees = addPeople.map(List::stream).orElse(Stream.empty());
            Optional<MeetingDetail> meetingDetailEntity1 = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId()).stream().findFirst();
            MeetingDetail meetingDetailEntity = meetingDetailEntity1.get();
            List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(meetingDetailEntity.getMeetingDate());
            Time startTime = meetingDetailEntity.getMeetingStartTime();
            Time endTime = meetingDetailEntity.getMeetingEndTime();
            List<MeetingDetail> overlappingMeetings = meetingsOnSpecificDate.stream()
                    .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                    .filter(overlappingMeeting -> (overlappingMeeting.getMeeting().getMeetingId() != meetingId))
                    .distinct()
                    .collect(Collectors.toList());
            List<Long> employeesOnMeetingDuringRequestTime = overlappingMeetings.stream()
                    .map(meetingDetail -> meetingDetail.getEmployee().getEmployeeId())
                    .toList();
            boolean ifEmployeesAlreadyBusy = addEmployees
                    .anyMatch(employeesOnMeetingDuringRequestTime::contains);
            if (ifEmployeesAlreadyBusy)
                throw new MeetingException("Members in some other meeting during this time");
            Stream<Integer> addEmployees1 = addPeople.map(List::stream).orElse(Stream.empty());
            addEmployees1
                    .forEach(addEmployee -> {
                        Employee employee = employeeRepository.findByEmployeeId(addEmployee);
                        if (employee == null)
                            throw new MeetingException("No such employee");
                        MeetingDetail meetingDetail = new MeetingDetail(meeting.get(), meetingDetailEntity.getMeetingDate(), startTime, endTime, employee);
                        meetingDetailRepository.save(meetingDetail);
                    });
            meeting.get().setCount((int) (meeting.get().getCount() + addPeople.stream().count()));
        }
        meetingRepository.save(meeting.get());
        return "People updated";
    }
}