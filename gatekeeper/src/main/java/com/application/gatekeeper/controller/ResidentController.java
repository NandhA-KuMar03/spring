package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.request.VisitorRegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;
import com.application.gatekeeper.response.AuthenticationResponse;
import com.application.gatekeeper.service.GatekeeperService;
import com.application.gatekeeper.service.ResidentService;
import com.application.gatekeeper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class ResidentController implements ResidentOperations{

    private ResidentService residentService;
    private UserService userService;

    @Autowired
    public ResidentController(ResidentService residentService, UserService userService) {
        this.residentService = residentService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDetails> createUser(RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(LoginRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(request));
    }

    @Override
    public ResponseEntity<Visitor> createAndScheduleVisitor(VisitorRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(residentService.createAndScheduleVisitor(request));
    }

    @Override
    public ResponseEntity<VisitorDetails> scheduleVisitor(VisitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(residentService.scheduleVisitor(request));
    }

    @Override
    public ResponseEntity<Blacklist> blackListVisitor(BlacklistRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(residentService.blackListVisitor(request));
    }

    @Override
    public ResponseEntity<List<VisitorDetails>> getVisitors() {
        return ResponseEntity.status(HttpStatus.OK).body(residentService.getVisitors());
    }

    @Override
    public ResponseEntity<List<VisitorDetails>> getActiveVisitors() {
        return ResponseEntity.status(HttpStatus.OK).body(residentService.getActiveVisitors());
    }
}
