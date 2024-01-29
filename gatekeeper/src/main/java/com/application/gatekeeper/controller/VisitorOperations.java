package com.application.gatekeeper.controller;

import com.application.gatekeeper.response.VisitorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

public interface VisitorOperations {

    @PutMapping("/entry")
    ResponseEntity<VisitorResponse> visitorEntry(HttpServletRequest request);

}