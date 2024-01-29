package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.request.EditInfoRequest;
import com.application.gatekeeper.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class AdminController implements AdminOperations{

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public ResponseEntity<List<User>> getAwaitingApprovals() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAwaitingApprovals());
    }

    @Override
    public ResponseEntity<UserDetails> getUserAwaitingApproval(int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getUserAwaitingApproval(userId));
    }

    @Override
    public ResponseEntity<User> approveUser(int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.approveUser(userId));
    }

    @Override
    public ResponseEntity<UserDetails> editInfo(EditInfoRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.editInfo(request));
    }

    @Override
    public void removeUser(int userId) {
        adminService.removeUser(userId);
    }
}
