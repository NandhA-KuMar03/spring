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
import com.application.gatekeeper.request.ApproveVisitorRequest;
import com.application.gatekeeper.request.BlacklistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR_DETAIL;

@Service
public class GatekeeperServiceImpl implements GatekeeperService {

    private VisitorDetailsRepository visitorDetailsRepository;
    private VisitorRepository visitorRepository;
    private BlacklistRepository blacklistRepository;
    private UserRepository userRepository;

    @Autowired
    public GatekeeperServiceImpl(VisitorDetailsRepository visitorDetailsRepository, VisitorRepository visitorRepository, BlacklistRepository blacklistRepository, UserRepository userRepository) {
        this.visitorDetailsRepository = visitorDetailsRepository;
        this.visitorRepository = visitorRepository;
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
    }

    VisitorDetails getVisitorDetails(int visitorDetailId){
        Optional<VisitorDetails> visitorDetails = visitorDetailsRepository.findById(visitorDetailId);
        if (! visitorDetails.isPresent())
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR_DETAIL);
        return visitorDetails.get();
    }

    Visitor checkForVisitor(int visitorId){
        Optional<Visitor> visitor = visitorRepository.findById(visitorId);
        if (! visitor.isPresent())
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR);
        return visitor.get();
    }

    @Override
    public List<VisitorDetails> getVisitorsOnDate(Date date) {
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByDateOfVisit(date);
        return visitorDetails;
    }

    @Override
    public VisitorDetails respondToVisitorScheduleRequest(ApproveVisitorRequest request) {
        int visitorDetailId = request.getVisitorDetailId();
        VisitorDetails visitorDetails = getVisitorDetails(visitorDetailId);
        visitorDetails.setApproved(Boolean.parseBoolean(request.getIsApproved()));
        visitorDetailsRepository.save(visitorDetails);
        return visitorDetails;
    }

    @Override
    public List<VisitorDetails> getAllVisitorDetailsOfParticularVisitor(int visitorId) {
        Visitor visitor = checkForVisitor(visitorId);
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByVisitorVisitorId(visitorId);
        return visitorDetails;
    }

    @Override
    public List<VisitorDetails> getAllVisitorDetailsOfParticularVisitorActiveData(int visitorId) {
        Visitor visitor = checkForVisitor(visitorId);
        LocalDate lt = LocalDate.now();
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisitAfter(visitorId, Date.valueOf(lt));
        return visitorDetails;
    }

    @Override
    public Blacklist blackListVisitor(BlacklistRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        Optional<Visitor> visitor = visitorRepository.findByEmail(request.getEmail());
        if (visitor.isEmpty())
            throw new GateKeeperApplicationException(NO_SUCH_VISITOR);
        visitor.get().setBlackListed(true);
        LocalDate lt = LocalDate.now();
        List<VisitorDetails> visitorDetails = visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisitAfter(visitor.get().getVisitorId(), Date.valueOf(lt));
        visitorDetails.stream()
                        .forEach(visitorDetail -> visitorDetail.setApproved(false));
        Blacklist blacklist = new Blacklist();
        blacklist.setVisitor(visitor.get());
        visitor.get().setBlackListed(true);
        blacklist.setReason(request.getReason());
        blacklist.setUser(user.get());
        visitorDetailsRepository.saveAll(visitorDetails);
        visitorRepository.save(visitor.get());
        return blacklistRepository.save(blacklist);
    }
}
