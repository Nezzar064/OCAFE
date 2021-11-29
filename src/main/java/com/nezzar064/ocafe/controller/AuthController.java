package com.nezzar064.ocafe.controller;

import com.nezzar064.ocafe.exception.TokenRefreshException;
import com.nezzar064.ocafe.model.entity.RefreshToken;
import com.nezzar064.ocafe.payload.request.ChangePasswordRequest;
import com.nezzar064.ocafe.payload.request.LoginRequest;
import com.nezzar064.ocafe.payload.request.SignupRequest;
import com.nezzar064.ocafe.payload.response.IsSignedInResponse;
import com.nezzar064.ocafe.payload.response.JwtResponse;
import com.nezzar064.ocafe.payload.response.SignUpResponse;
import com.nezzar064.ocafe.payload.response.TokenRefreshResponse;
import com.nezzar064.ocafe.security.service.RefreshTokenService;
import com.nezzar064.ocafe.security.service.UserDetailsImpl;
import com.nezzar064.ocafe.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        userService.getAndSetAuthentication(loginRequest);
        UserDetailsImpl userDetails = userService.getUserDetails(loginRequest);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        //TODO: redo this response
        return ResponseEntity.ok(new JwtResponse(
                userService.generateJwt(userDetails),
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(), roles));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshResponse tokenRefreshResponse) {
        String refreshToken = tokenRefreshResponse.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = userService.generateJwtFromUsername(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        userService.createUser(signUpRequest);
        return ResponseEntity.ok(new SignUpResponse("User registered successfully!"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> validateSignIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new IsSignedInResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }


}

