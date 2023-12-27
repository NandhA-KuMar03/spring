package com.spring.rest.meetingscheduler.serviceimpl;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.exception.MeetingException;
import com.spring.rest.meetingscheduler.repository.EmployeeRepository;
import com.spring.rest.meetingscheduler.repository.MeetingDetailRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRepository;
import com.spring.rest.meetingscheduler.repository.MeetingRoomRepository;
import com.spring.rest.meetingscheduler.repository.TeamRepository;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.response.MeetingsOnSpecificDateResponse;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.spring.rest.meetingscheduler.constants.CommonConstants.CANNOT_DELETE_MEETING;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.CAPACITY_NOT_ENOUGH;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.DIFFERENT_MEETING_SCHEDULED;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.EMPLOYEE_NOT_IN_MEETING;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.ENTER_VALID_MEETING;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.ENTER_VALID_ROOM;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.INVALID_FORMAT;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.MEMBERS_IN_SOME_OTHER_MEETING;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.NO_SUCH_EMPLOYEE;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.NO_SUCH_MEETING;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.ROOM_ALREADY_BOOKED;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.ROOM_ALREADY_OCCUPIED;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.ROOM_HAS_LOWER_CAPACITY;
import static com.spring.rest.meetingscheduler.constants.CommonConstants.START_AFTER_ENDTIME;

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

    public Boolean isTime(String inputTime){
        try {
            Time time = Time.valueOf(inputTime);
        }catch (Exception e){
            return false;
        }
        return true;
    }
/*  Get available rooms service
    Params: Date of the meeting, start time, end time, number of attendees
    Returns List of available rooms and capacity
        Get all the meetings happening at the given date
        Check if any meetings overlap during the given timings
        Get all rooms other than overlapping meetings & only rooms which have capacity greater than number of attendees
*/
    @Override
    public List<MeetingRoom> getAvailableRooms(MeetingRequestObject meetingDetails) {
        if(! ((GenericValidator.isDate(meetingDetails.getMeetingDate(),"yyyy-mm-dd",true)) || (isTime(meetingDetails.getMeetingStartTime())) || (isTime(meetingDetails.getMeetingEndTime()))))
            throw new MeetingException(INVALID_FORMAT);
        Date date = Date.valueOf(LocalDate.parse(meetingDetails.getMeetingDate()));
        Time startTime = Time.valueOf(meetingDetails.getMeetingStartTime());
        Time endTime = Time.valueOf(meetingDetails.getMeetingEndTime());
        int count = meetingDetails.getCount();

        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(date);
        List<Integer> bookedRoomIds = meetingsOnSpecificDate.stream()
                        .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                        .map(meetingDetail -> meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId())
                        .distinct()
                        .collect(Collectors.toList());
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        meetingRooms.removeIf(meetingRoom -> bookedRoomIds.contains(meetingRoom.getMeetingRoomId()));
        meetingRooms.removeIf(meetingRoom -> meetingRoom.getCapacity() < count);
        Comparator<MeetingRoom> comparator = Comparator.comparing(MeetingRoom::getCapacity);
        List<MeetingRoom> meetingRoomsSorted = meetingRooms.stream().sorted(comparator).collect(Collectors.toList());
        return meetingRoomsSorted;
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
    public MeetingResponse createMeeting(MeetingRequestObject meetingDetails) {
        if(! ((GenericValidator.isDate(meetingDetails.getMeetingDate(),"yyyy-mm-dd",true)) || (isTime(meetingDetails.getMeetingStartTime())) || (isTime(meetingDetails.getMeetingEndTime()))))
            throw new MeetingException(INVALID_FORMAT);
        int roomId = meetingDetails.getRoomId();
        int teamId = meetingDetails.getTeamId();
        String meetingName = meetingDetails.getMeetingName();
        Date date = Date.valueOf(LocalDate.parse(meetingDetails.getMeetingDate()));
        Time startTime = Time.valueOf(meetingDetails.getMeetingStartTime());
        Time endTime = Time.valueOf(meetingDetails.getMeetingEndTime());
        List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(date);
        List<MeetingDetail> overlappingMeetings = meetingsOnSpecificDate.stream()
                .filter(meetingDetail -> meetingDetail.getMeetingStartTime().before(endTime) && startTime.before(meetingDetail.getMeetingEndTime()))
                .distinct()
                .collect(Collectors.toList());
        long numberOfEmployeesAlreadyOccupied = overlappingMeetings.stream()
                .filter(meetingDetail -> meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId() == roomId).count();
        if((int) numberOfEmployeesAlreadyOccupied >0)
            throw new MeetingException(ROOM_ALREADY_BOOKED);

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
            throw new MeetingException(MEMBERS_IN_SOME_OTHER_MEETING);

        MeetingRoom meetingRoom = meetingRoomRepository.findByMeetingRoomId(roomId);
        int capacity = meetingRoom.getCapacity();
        long numberOfAttendees = employeeIds.stream().count();
        if(capacity < numberOfAttendees)
            throw new MeetingException(CAPACITY_NOT_ENOUGH);
        Meeting meeting1;
        try {
            Meeting meeting = new Meeting(meetingName, "ACTIVE", (int) numberOfAttendees, meetingRoom);
            meeting1 = meetingRepository.save(meeting);
            employees.stream()
                    .forEach(employee -> {
                        MeetingDetail meetingDetail = new MeetingDetail(meeting, date, startTime, endTime, employee);
                        meetingDetailRepository.save(meetingDetail);
                    });
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setMeetingId((int) meeting1.getMeetingId());
        meetingResponse.setMeetingName(meetingName);
        meetingResponse.setMeetingDate(date);
        meetingResponse.setMeetingStartTime(startTime);
        meetingResponse.setMeetingEndTime(endTime);
        meetingResponse.setEmployeeId(employeeIds);
        meetingResponse.setRoomName(meetingRoom.getRoomName());
        meetingResponse.setRoomId(meetingRoom.getMeetingRoomId());
        return meetingResponse;
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
    public MeetingResponse cancelMeeting(int meetingId) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if(meeting.isEmpty())
            throw new MeetingException(NO_SUCH_MEETING);
        List<MeetingDetail> meetingDetails = meetingDetailRepository.findByMeetingMeetingId(meetingId);
        LocalDate meetingDate = meetingDetails.get(0).getMeetingDate().toLocalDate();
        LocalTime meetingTime = meetingDetails.get(0).getMeetingStartTime().toLocalTime();
        LocalDateTime meetingDateTime = LocalDateTime.of(meetingDate,meetingTime);
        Duration duration = Duration.between(now, meetingDateTime);
        if(duration.isNegative() || duration.toMinutes() < 30)
            throw new MeetingException(CANNOT_DELETE_MEETING);
        meetingDetails.stream()
                .forEach(meetingDetail -> meetingDetailRepository.delete(meetingDetail));
        meetingRepository.delete(meeting.get());
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setMeetingId((int) meeting.get().getMeetingId());
        meetingResponse.setRoomId(meeting.get().getMeetingRoom().getMeetingRoomId());
        return meetingResponse;
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
    public MeetingResponse updateMeeting(int meetingId, Date date, String meetingName, Time startTime, Time endTime) {
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if(meeting.isEmpty())
            throw new MeetingException(ENTER_VALID_MEETING);
        List<MeetingDetail> meetingDetailEntity = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId());
        Optional<MeetingDetail> meetingDetail1 = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId()).stream().findFirst();
        Time newStartTime = meetingDetail1.get().getMeetingStartTime();
        Time newEndTime = meetingDetail1.get().getMeetingEndTime();
        Date newDate = meetingDetail1.get().getMeetingDate();
        List<MeetingDetail> employeeId = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId());
        List<Long> employeeIds = employeeId.stream().map(employeed -> employeed.getEmployee().getEmployeeId()).collect(Collectors.toList());

        if(meetingName != null)
            meeting.get().setMeetingName(meetingName);


        if ((date != null) || (startTime != null) || (endTime != null)){
            if(date != null){
                newDate = date;
            }
            if(startTime != null){
                newStartTime = startTime;
            }
            if(endTime != null){
                newEndTime = endTime;
            }
            List<MeetingDetail> meetingsOnSpecificDate = meetingDetailRepository.findByMeetingDate(newDate);
            Time finalNewEndTime = newEndTime;
            Time finalNewStartTime = newStartTime;
            Date finalNewDate = newDate;
            if(finalNewStartTime.after(finalNewEndTime))
                throw new MeetingException(START_AFTER_ENDTIME);
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
                throw new MeetingException(MEMBERS_IN_SOME_OTHER_MEETING);
            if((int) n > 0)
                throw new MeetingException(DIFFERENT_MEETING_SCHEDULED);

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
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setMeetingId((int) meeting.get().getMeetingId());
        meetingResponse.setMeetingName(meetingName);
        meetingResponse.setMeetingDate(newDate);
        meetingResponse.setMeetingStartTime(newStartTime);
        meetingResponse.setMeetingEndTime(newEndTime);
        meetingResponse.setRoomName(meeting.get().getMeetingRoom().getRoomName());
        meetingResponse.setRoomId(meeting.get().getMeetingRoom().getMeetingRoomId());
        meetingResponse.setEmployeeId(employeeIds);
        return meetingResponse;
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
    public MeetingResponse updateRoom(int meetingId, int roomId) {
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if(meeting.isEmpty())
            throw new MeetingException(ENTER_VALID_MEETING);
        List<MeetingDetail> employeeId = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId());
        List<Long> employeeIds = employeeId.stream().map(employeed -> employeed.getEmployee().getEmployeeId()).collect(Collectors.toList());
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
            throw new MeetingException(ROOM_ALREADY_OCCUPIED);


        MeetingRoom meetingRoom = meetingRoomRepository.findByMeetingRoomId(roomId);
        if(meetingRoom.getCapacity() < meeting.get().getCount())
            throw new MeetingException(ROOM_HAS_LOWER_CAPACITY);
        try{
            meeting.get().setMeetingRoom(meetingRoom);
            meetingRepository.save(meeting.get());
            MeetingResponse meetingResponse = new MeetingResponse();
            meetingResponse.setEmployeeId(employeeIds);
            meetingResponse.setMeetingId((int) meeting.get().getMeetingId());
            meetingResponse.setRoomName(meetingRoom.getRoomName());
            meetingResponse.setRoomId(meetingRoom.getMeetingRoomId());
            return meetingResponse;
        }
        catch (Exception e){
            throw new RuntimeException(ENTER_VALID_ROOM);
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
    public MeetingResponse updatePeople(int meetingId, List<Integer> addPeople, List<Integer> removePeople) {
        Optional<Meeting> meeting = meetingRepository.findByMeetingId(meetingId);
        if (meeting.isEmpty())
            throw new MeetingException(ENTER_VALID_MEETING);

        try {
            Stream<Integer> removeEmployees = removePeople.stream();
            removeEmployees.map(MeetingDetail -> meetingDetailRepository.findByEmployeeEmployeeIdAndMeetingMeetingId(MeetingDetail, meetingId))
                    .forEach(meetingDetail -> meetingDetailRepository.delete(meetingDetail));
        }
        catch (Exception e) {
            throw new MeetingException(EMPLOYEE_NOT_IN_MEETING);
        }
        meeting.get().setCount((int) (meeting.get().getCount() - removePeople.stream().count()));

        Stream<Integer> addEmployees = addPeople.stream();
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
        System.out.println(overlappingMeetings);
        List<Integer> employeesOnMeetingDuringRequestTime = overlappingMeetings.stream()
                .map(meetingDetail -> (int) meetingDetail.getEmployee().getEmployeeId())
                .toList();
        System.out.println(employeesOnMeetingDuringRequestTime);
        List<Integer> emp = addEmployees.filter(employeesOnMeetingDuringRequestTime::contains).collect(Collectors.toList());
        System.out.println(emp);
        if (! emp.isEmpty())
            throw new MeetingException(MEMBERS_IN_SOME_OTHER_MEETING);
        Stream<Integer> addEmployees1 = addPeople.stream();
        addEmployees1
                .forEach(addEmployee -> {
                    Employee employee = employeeRepository.findByEmployeeId(addEmployee);
                    if (employee == null)
                        throw new MeetingException(NO_SUCH_EMPLOYEE);
                    MeetingDetail meetingDetail = new MeetingDetail(meeting.get(), meetingDetailEntity.getMeetingDate(), startTime, endTime, employee);
                    meetingDetailRepository.save(meetingDetail);
                });
        meeting.get().setCount((int) (meeting.get().getCount() + addPeople.stream().count()));

        meetingRepository.save(meeting.get());
        List<MeetingDetail> employeeId = meetingDetailRepository.findByMeetingMeetingId(meeting.get().getMeetingId());
        Optional<MeetingDetail> meetingDetail = meetingDetailRepository.findByMeetingMeetingId(meetingId).stream().findFirst();
        List<Long> employeeIds = employeeId.stream().map(employeed -> employeed.getEmployee().getEmployeeId()).collect(Collectors.toList());
        MeetingResponse meetingResponse = new MeetingResponse();
        meetingResponse.setEmployeeId(employeeIds);
        meetingResponse.setMeetingName(meeting.get().getMeetingName());
        meetingResponse.setMeetingDate(meetingDetail.get().getMeetingDate());
        meetingResponse.setMeetingStartTime(meetingDetail.get().getMeetingStartTime());
        meetingResponse.setMeetingEndTime(meetingDetail.get().getMeetingEndTime());
        meetingResponse.setMeetingId(meetingId);
        meetingResponse.setRoomName(meeting.get().getMeetingRoom().getRoomName());
        meetingResponse.setRoomId(meeting.get().getMeetingRoom().getMeetingRoomId());
        return meetingResponse;
    }

    @Override
    public List<MeetingsOnSpecificDateResponse> getMeetings(Date date) {
        List<MeetingsOnSpecificDateResponse> response = new ArrayList<>();
        List<MeetingDetail> meetingDetails = meetingDetailRepository.findByMeetingDate(date);
        List<Long> meetingIds = meetingDetails.stream().map(empId -> empId.getMeeting().getMeetingId()).collect(Collectors.toList());
        meetingIds = meetingIds.stream().distinct().collect(Collectors.toList());

        meetingIds.stream().forEach(meetingId -> {
            MeetingsOnSpecificDateResponse meetings = new MeetingsOnSpecificDateResponse();
            MeetingDetail meetingDetail = meetingDetailRepository.findFirstByMeetingMeetingId(meetingId);
            meetings.setMeetingId((int) meetingDetail.getMeeting().getMeetingId());
            meetings.setMeetingName(meetingDetail.getMeeting().getMeetingName());
            meetings.setStartTime(meetingDetail.getMeetingStartTime());
            meetings.setEndTime(meetingDetail.getMeetingEndTime());
            meetings.setRoomId(meetingDetail.getMeeting().getMeetingRoom().getMeetingRoomId());
            meetings.setRoomName(meetingDetail.getMeeting().getMeetingRoom().getRoomName());
            response.add(meetings);
        });
        return response;
    }
}