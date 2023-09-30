package com.fcynnek.PetManagement.security;

import com.fcynnek.PetManagement.dao.request.SignInRequest;
import com.fcynnek.PetManagement.dao.request.SignUpRequest;
import com.fcynnek.PetManagement.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}