package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.ApproveVisitorRequest;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.service.GatekeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("api")
public class GatekeeperController implements GatekeeperOperations{

    private GatekeeperService gatekeeperService;

    @Autowired
    public GatekeeperController(GatekeeperService gatekeeperService) {
        this.gatekeeperService = gatekeeperService;
    }

    @Override
    public ResponseEntity<List<VisitorDetails>> getVisitorsOnDate(Date date) {
        return ResponseEntity.status(HttpStatus.OK).body(gatekeeperService.getVisitorsOnDate(date));
    }

    @Override
    public ResponseEntity<VisitorDetails> respondToVisitorScheduleRequest(ApproveVisitorRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(gatekeeperService.respondToVisitorScheduleRequest(request));
    }

    @Override
    public ResponseEntity<List<VisitorDetails>> getAllVisitorDetailsOfParticularVisitor(int visitorId) {
        return ResponseEntity.status(HttpStatus.OK).body(gatekeeperService.getAllVisitorDetailsOfParticularVisitor(visitorId));
    }

    @Override
    public ResponseEntity<List<VisitorDetails>> getAllVisitorDetailsOfParticularVisitorActiveData(int visitorId) {
        return ResponseEntity.status(HttpStatus.OK).body(gatekeeperService.getAllVisitorDetailsOfParticularVisitorActiveData(visitorId));
    }

    @Override
    public ResponseEntity<Blacklist> blackListVisitor(BlacklistRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(gatekeeperService.blackListVisitor(request));
    }
}
