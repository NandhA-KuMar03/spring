package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.ApproveVisitorRequest;
import com.application.gatekeeper.request.BlacklistRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

public interface GatekeeperOperations {

    @GetMapping("/gatekeeper/visitors")
    ResponseEntity<List<VisitorDetails>> getVisitorsOnDate(@RequestParam Date date);

    @PatchMapping("/gatekeeper/visitors")
    ResponseEntity<VisitorDetails> respondToVisitorScheduleRequest(@RequestBody ApproveVisitorRequest request);

    @GetMapping("/gatekeeper/visitor/history")
    ResponseEntity<List<VisitorDetails>> getAllVisitorDetailsOfParticularVisitor(@RequestParam int visitorId);

    @GetMapping("/gatekeeper/visitor/active")
    ResponseEntity<List<VisitorDetails>> getAllVisitorDetailsOfParticularVisitorActiveData(@RequestParam int visitorId);

    @PutMapping("/gatekeeper/blacklist")
    ResponseEntity<Blacklist> blackListVisitor(@RequestBody BlacklistRequest request);
}
