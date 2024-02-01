package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.request.EditInfoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface AdminOperations {

    @GetMapping("/admin/approvals")
    ResponseEntity<List<User>> getAwaitingApprovals();

    @GetMapping("/admin/approvals/{userId}")
    ResponseEntity<UserDetails> getUserAwaitingApproval(@PathVariable("userId") int userId);

    @PutMapping("/admin/approvals")
    ResponseEntity<User> approveUser(@RequestParam int userId);

    @PutMapping("/admin/info")
    ResponseEntity<UserDetails> editInfo(@RequestBody EditInfoRequest request);

    @PatchMapping("/admin/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeUser(@RequestParam int userId);

}