package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.exception.GateKeeperApplicationException;
import com.application.gatekeeper.repository.BlacklistRepository;
import com.application.gatekeeper.repository.UserRepository;
import com.application.gatekeeper.repository.VisitorDetailsRepository;
import com.application.gatekeeper.repository.VisitorRepository;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.VisitorRegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.CommonConstants.ROLE_GATEKEEPER;
import static com.application.gatekeeper.constants.CommonConstants.ROLE_RESIDENT;
import static com.application.gatekeeper.constants.ErrorConstants.ALREADY_BLACKLISTED;
import static com.application.gatekeeper.constants.ErrorConstants.GATEKEEPER_CANNOT_CREATE_VISITOR;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.TIME_OVER;
import static com.application.gatekeeper.constants.ErrorConstants.USER_DEACTIVATED;
import static com.application.gatekeeper.constants.ErrorConstants.USER_NOT_APPROVED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_ALREADY_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BLOCKED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BUSY;

@Service
public class ResidentServiceImpl implements ResidentService{

    private VisitorRepository visitorRepository;
    private VisitorDetailsRepository visitorDetailsRepository;
    private BlacklistRepository blacklistRepository;
    private UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public ResidentServiceImpl(VisitorRepository visitorRepository, VisitorDetailsRepository visitorDetailsRepository, BlacklistRepository blacklistRepository, UserRepository userRepository, JwtService jwtService) {
        this.visitorRepository = visitorRepository;
        this.visitorDetailsRepository = visitorDetailsRepository;
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    Optional<Visitor> checkVisitorExists(String email){
        Optional<Visitor> visitor = visitorRepository.findByEmail(email);
        return visitor;
    }

    void checkUserApproval(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        if (! user.get().isApproved())
            throw new GateKeeperApplicationException(USER_NOT_APPROVED);
        if (! user.get().isActive())
            throw new GateKeeperApplicationException(USER_DEACTIVATED);
    }

    @Override
    public Visitor createAndScheduleVisitor(VisitorRegisterRequest request) {
        checkUserApproval();
        Optional<Visitor> visitorCheck = checkVisitorExists(request.getEmail());
        if (visitorCheck.isPresent())
            throw new GateKeeperApplicationException(VISITOR_ALREADY_EXISTS);
        Visitor visitor = new Visitor();
        visitor.setBlackListed(false);
        visitor.setVisitorName(request.getName());
        visitor.setEmail(request.getEmail());
        Visitor responseVisitor = visitorRepository.save(visitor);
        return responseVisitor;
    }

    @Override
    public VisitorDetails scheduleVisitor(VisitorRequest request) {
        checkUserApproval();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        Optional<Visitor> visitor = checkVisitorExists(request.getEmail());
        if (visitor.isEmpty())
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR_EXISTS);
        if (visitor.get().isBlackListed()){
            List<Blacklist> blacklists = blacklistRepository.findAllByVisitorEmail(request.getEmail());
            System.out.println("Blacklists" + blacklists);
            boolean blackListedByGatekeeper =  blacklists.stream().anyMatch(blacklist ->
               blacklist.getUser().getRoles().get(0).getRoleName() == ROLE_GATEKEEPER
            );
            boolean blackListedByUser = blacklists.stream().anyMatch(blacklist ->
                    blacklist.getUser().getUserId() == user.get().getUserId()
            );
            if (blackListedByGatekeeper || blackListedByUser)
                throw new GateKeeperApplicationException(VISITOR_BLOCKED);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime visitorDateTime = LocalDateTime.of(request.getDateOfVisit().toLocalDate(), request.getTimeOfEntry().toLocalTime());
        Duration duration = Duration.between(now, visitorDateTime);
        if (duration.isNegative())
            throw new GateKeeperApplicationException(TIME_OVER);
        List<VisitorDetails> visitorOnThatDate = visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisit(visitor.get().getVisitorId(), request.getDateOfVisit());
        long n = visitorOnThatDate.stream()
                .filter(visitorCheck -> visitorCheck.getTimeOfEntry().before(request.getTimeOfExit()) && request.getTimeOfEntry().before(visitorCheck.getTimeOfExit()))
                .count();
        if (n > 0)
            throw new GateKeeperApplicationException(VISITOR_BUSY);
        LocalDate localDateEntry = request.getDateOfVisit().toLocalDate();
        LocalDateTime dateTime  = localDateEntry.atTime(request.getTimeOfEntry().toLocalTime());
        Instant instantStart = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date dateEntry = java.util.Date.from(instantStart);
        LocalDate localDateExit = request.getDateOfVisit().toLocalDate();
        LocalDateTime dateTimeExit  = localDateExit.atTime(request.getTimeOfExit().toLocalTime());
        Instant instantEnd = dateTimeExit.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date dateExit = java.util.Date.from(instantEnd);
        System.out.println(dateEntry +" "+ dateExit);
        VisitorDetails visitorDetails = new VisitorDetails();
        var jwtToken = jwtService.generateTokenForVisitor(user.get(), dateEntry, dateExit, request.getEmail());
        visitorDetails.setVisitorPasskey(jwtToken);
        System.out.println(jwtToken);
        visitorDetails.setVisitor(visitor.get());
        visitorDetails.setDateOfVisit(request.getDateOfVisit());
        visitorDetails.setTimeOfEntry(request.getTimeOfEntry());
        visitorDetails.setTimeOfExit(request.getTimeOfExit());
        visitorDetails.setUser(user.get());
        return visitorDetailsRepository.save(visitorDetails);
    }

    @Override
    public Blacklist blackListVisitor(BlacklistRequest request) {
        checkUserApproval();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        Optional<Visitor> visitor = checkVisitorExists(request.getEmail());
        if (visitor.isEmpty())
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR_EXISTS);
        Optional<Blacklist> blacklistExists = blacklistRepository.findByVisitorVisitorIdAndUserUserId(visitor.get().getVisitorId(), user.get().getUserId());
        if (blacklistExists.isPresent())
            throw new GateKeeperApplicationException(ALREADY_BLACKLISTED);
        visitor.get().setBlackListed(true);
        Blacklist blacklist = new Blacklist();
        blacklist.setReason(request.getReason());
        blacklist.setVisitor(visitor.get());
        blacklist.setUser(user.get());
        visitorRepository.save(visitor.get());
        return blacklistRepository.save(blacklist);
    }

    @Override
    public List<VisitorDetails> getVisitors() {
        checkUserApproval();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByUserUserId(user.get().getUserId());
        return visitorDetails;
    }

    @Override
    public List<VisitorDetails> getActiveVisitors() {
        checkUserApproval();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        LocalDate now = LocalDate.now();
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByUserUserIdAndDateOfVisitAfter(user.get().getUserId(), Date.valueOf(now));
        return visitorDetails;
    }
}
