package com.fcynnek.PetManagement.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fcynnek.PetManagement.dao.request.SignInRequest;
import com.fcynnek.PetManagement.dao.request.SignUpRequest;
import com.fcynnek.PetManagement.dao.request.TokenRefreshRequest;
import com.fcynnek.PetManagement.dao.response.JwtAuthenticationResponse;
import com.fcynnek.PetManagement.dao.response.TokenRefreshResponse;
import com.fcynnek.PetManagement.domain.RefreshToken;
import com.fcynnek.PetManagement.security.AuthenticationService;
import com.fcynnek.PetManagement.security.JwtService;
import com.fcynnek.PetManagement.service.RefreshTokenService;

@RestController
//@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    
    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        super();
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
    
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
      String requestRefreshToken = request.refreshToken();

      return refreshTokenService.findByToken(requestRefreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getUser)
          .map(user -> {
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
          })
          .orElseThrow(() -> new IllegalStateException(
              "Refresh token " + requestRefreshToken + " is not in database!"));
    }
}