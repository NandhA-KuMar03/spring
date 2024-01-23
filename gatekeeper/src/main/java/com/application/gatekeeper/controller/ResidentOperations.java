package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;
import com.application.gatekeeper.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ResidentOperations {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> createUser(@RequestBody RegisterRequest request);

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request);

    @PostMapping("/resident/create")
    ResponseEntity<VisitorDetails> createAndScheduleVisitor(@RequestBody VisitorRequest request);

    @PostMapping("/resident/visitor/schedule")
    ResponseEntity<VisitorDetails> scheduleVisitor(@RequestBody VisitorRequest request);

    @PutMapping("/resident/blacklist")
    ResponseEntity<Blacklist> blackListVisitor(@RequestBody BlacklistRequest request);

    @GetMapping("/resident/visitors")
    ResponseEntity<List<VisitorDetails>> getVisitors();

    @GetMapping("/resident/visitors/active")
    ResponseEntity<List<VisitorDetails>> getActiveVisitors();
    
}
