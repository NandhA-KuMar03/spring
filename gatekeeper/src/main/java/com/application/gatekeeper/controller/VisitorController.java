package com.application.gatekeeper.controller;

import com.application.gatekeeper.response.VisitorResponse;
import com.application.gatekeeper.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class VisitorController implements VisitorOperations{

    private VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @Override
    public ResponseEntity<VisitorResponse> visitorEntry(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(visitorService.visitorEntry(request));
    }
}
