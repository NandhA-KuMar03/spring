package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.exception.GateKeeperApplicationException;
import com.application.gatekeeper.repository.BlacklistRepository;
import com.application.gatekeeper.repository.VisitorDetailsRepository;
import com.application.gatekeeper.repository.VisitorRepository;
import com.application.gatekeeper.response.VisitorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.CommonConstants.ROLE_GATEKEEPER;
import static com.application.gatekeeper.constants.CommonConstants.VISITOR_VISITED;
import static com.application.gatekeeper.constants.ErrorConstants.JWT_TOKEN_MISSING;
import static com.application.gatekeeper.constants.ErrorConstants.TIME_MISMATCH;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BLACKLISTED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BLOCKED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_NOT_APPROVED;

@Service
public class VisitorServiceImpl implements VisitorService{

    private JwtService jwtService;
    private VisitorDetailsRepository visitorDetailsRepository;
    private BlacklistRepository blacklistRepository;
    private VisitorRepository visitorRepository;

    @Autowired
    public VisitorServiceImpl(JwtService jwtService, VisitorDetailsRepository visitorDetailsRepository, BlacklistRepository blacklistRepository, VisitorRepository visitorRepository) {
        this.jwtService = jwtService;
        this.visitorDetailsRepository = visitorDetailsRepository;
        this.blacklistRepository = blacklistRepository;
        this.visitorRepository = visitorRepository;
    }

    @Override
    public VisitorResponse visitorEntry(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new GateKeeperApplicationException(JWT_TOKEN_MISSING);
        }
        jwtToken = authHeader.substring(7);
        boolean isValid = jwtService.isVisitorValid(jwtToken);
        if (! isValid)
            throw new GateKeeperApplicationException(TIME_MISMATCH);
        VisitorDetails visitorDetails = visitorDetailsRepository.findByVisitorPasskey(jwtToken);
        if (! visitorDetails.isApproved())
            throw new GateKeeperApplicationException(VISITOR_NOT_APPROVED);
        Optional<Visitor> visitor = visitorRepository.findByEmail(visitorDetails.getVisitor().getEmail());
        if (visitor.get().isBlackListed()){
            List<Blacklist> blacklists = blacklistRepository.findAllByVisitorEmail(visitorDetails.getVisitor().getEmail());
            boolean blackListedByGatekeeper =  blacklists.stream().anyMatch(blacklist ->
                    blacklist.getUser().getRoles().get(0).getRoleName() == ROLE_GATEKEEPER
            );
            boolean blackListedByUser = blacklists.stream().anyMatch(blacklist ->
                    blacklist.getUser().getUserId() == visitorDetails.getUser().getUserId()
            );
            if (blackListedByGatekeeper || blackListedByUser)
                throw new GateKeeperApplicationException(VISITOR_BLACKLISTED);
        }
        visitorDetails.setHasVisited(true);
        visitorDetailsRepository.save(visitorDetails);
        return VisitorResponse.builder()
                        .message(VISITOR_VISITED).build();
    }
}
