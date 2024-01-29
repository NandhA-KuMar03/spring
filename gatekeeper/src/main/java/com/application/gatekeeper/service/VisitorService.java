package com.application.gatekeeper.service;

import com.application.gatekeeper.response.VisitorResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface VisitorService {
    VisitorResponse visitorEntry(HttpServletRequest request);
}
