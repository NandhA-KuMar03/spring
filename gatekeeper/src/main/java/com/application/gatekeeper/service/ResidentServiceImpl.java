package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.exception.GateKeeperApplicationException;
import com.application.gatekeeper.repository.BlacklistRepository;
import com.application.gatekeeper.repository.VisitorDetailsRepository;
import com.application.gatekeeper.repository.VisitorRepository;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.request.VisitorRegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;
import com.application.gatekeeper.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

import static com.application.gatekeeper.constants.CommonConstants.ROLE_GATEKEEPER;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_ALREADY_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BLOCKED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BUSY;

@Service
public class ResidentServiceImpl implements ResidentService{

    private VisitorRepository visitorRepository;
    private VisitorDetailsRepository visitorDetailsRepository;
    private BlacklistRepository blacklistRepository;

    @Autowired
    public ResidentServiceImpl(VisitorRepository visitorRepository, VisitorDetailsRepository visitorDetailsRepository, BlacklistRepository blacklistRepository) {
        this.visitorRepository = visitorRepository;
        this.visitorDetailsRepository = visitorDetailsRepository;
        this.blacklistRepository = blacklistRepository;
    }

    Visitor checkVisitorExists(String email){
        Visitor visitor = visitorRepository.findByEmail(email);
        return visitor;
    }

    @Override
    public Visitor createAndScheduleVisitor(VisitorRegisterRequest request) {
        Visitor visitorCheck = checkVisitorExists(request.getEmail());
        if (visitorCheck == null)
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
        Visitor visitor = checkVisitorExists(request.getEmail());
        if (! (visitor == null))
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR_EXISTS);
        if (visitor.isBlackListed()){
            List<Blacklist> blacklists = blacklistRepository.findAllByVisitorEmail(request.getEmail());
            boolean blackListedByGatekeeper =  blacklists.stream().anyMatch(blacklist ->
               blacklist.getUser().getRoles().get(0).getRoleName() == ROLE_GATEKEEPER
            );
            //check for user too
            if (blackListedByGatekeeper)
                throw new GateKeeperApplicationException(VISITOR_BLOCKED);
        }
        List<VisitorDetails> visitorOnThatDate = visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisit(visitor.getVisitorId(), request.getDateOfVisit());
        long n = visitorOnThatDate.stream()
                .filter(visitorCheck -> visitorCheck.getTimeOfEntry().before(request.getTimeOfExit()) && request.getTimeOfEntry().before(visitorCheck.getTimeOfExit()))
                .count();
        if (n > 0)
            throw new GateKeeperApplicationException(VISITOR_BUSY);
        VisitorDetails visitorDetails = new VisitorDetails();
        visitorDetails.setVisitor(visitor);
        visitorDetails.setDateOfVisit(request.getDateOfVisit());
        visitorDetails.setTimeOfEntry(request.getTimeOfEntry());
        visitorDetails.setTimeOfExit(request.getTimeOfExit());
        return visitorDetailsRepository.save(visitorDetails);
    }

    @Override
    public Blacklist blackListVisitor(BlacklistRequest request) {
        Visitor visitor = checkVisitorExists(request.getEmail());
        if (! (visitor == null))
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR_EXISTS);
        Blacklist blacklist = new Blacklist();
        blacklist.setReason(request.getReason());
        blacklist.setVisitor(visitor);
        //set User
        return blacklistRepository.save(blacklist);
    }

    @Override
    public List<VisitorDetails> getVisitors() {
        int userId = 1;
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByUserUserId(userId);
        return visitorDetails;
    }

    @Override
    public List<VisitorDetails> getActiveVisitors() {
        int userId = 1;
        Date date = new Date(System.currentTimeMillis());
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByUserUserIdAndDateOfVisitAfter(userId, date);
        return visitorDetails;
    }
}
