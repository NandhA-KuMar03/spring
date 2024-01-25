package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.response.AuthenticationResponse;

public interface UserService {

    UserDetails createUser(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);

}
