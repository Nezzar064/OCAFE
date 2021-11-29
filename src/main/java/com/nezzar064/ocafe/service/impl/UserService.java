package com.nezzar064.ocafe.service.impl;

import com.nezzar064.ocafe.exception.DataNotFoundException;
import com.nezzar064.ocafe.model.PersonRole;
import com.nezzar064.ocafe.model.UserRole;
import com.nezzar064.ocafe.model.dto.PersonDto;
import com.nezzar064.ocafe.model.entity.Role;
import com.nezzar064.ocafe.model.entity.User;
import com.nezzar064.ocafe.payload.request.LoginRequest;
import com.nezzar064.ocafe.payload.request.SignupRequest;
import com.nezzar064.ocafe.repository.RefreshTokenRepository;
import com.nezzar064.ocafe.repository.RoleRepository;
import com.nezzar064.ocafe.repository.UserRepository;
import com.nezzar064.ocafe.security.jwt.JwtUtils;
import com.nezzar064.ocafe.security.service.UserDetailsImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class UserService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public String generateJwt(UserDetailsImpl userDetails) {
        return jwtUtils.generateJwtToken(userDetails);
    }

    public String generateJwtFromUsername(User user) {
        return jwtUtils.generateTokenFromUsername(user.getUsername());
    }

    public void getAndSetAuthentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserDetailsImpl getUserDetails(LoginRequest loginRequest) {
        Authentication authentication = authorizeFromLoginRequest(loginRequest);
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public void createUser(SignupRequest signupRequest) {
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByUserRole(UserRole.ROLE_ADMIN)
                            .orElseThrow(() -> new DataNotFoundException("Error: Role is not found."));
                    roles.add(adminRole);
                    break;
                case "mod":
                    Role modRole = roleRepository.findByUserRole(UserRole.ROLE_MODERATOR)
                            .orElseThrow(() -> new DataNotFoundException("Error: Role is not found."));
                    roles.add(modRole);
                    break;
                default:
                    Role userRole = roleRepository.findByUserRole(UserRole.ROLE_USER)
                            .orElseThrow(() -> new DataNotFoundException("Error: Role is not found."));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userRepository.save(user);
    }

    //Handles user creation for a person based on their role
    //Teachers get username from range 200-999, students 2000-9999
    //TODO: Implement a password generator for this so it's not just "password".
    public User createUserForPerson(PersonDto personDto) {
        User user = new User();
        Role modRole = roleRepository.findByUserRole(UserRole.ROLE_MODERATOR)
                .orElseThrow(() -> new DataNotFoundException("Error: Role is not found."));
        Role userRole = roleRepository.findByUserRole(UserRole.ROLE_USER)
                .orElseThrow(() -> new DataNotFoundException("Error: Role is not found."));
        String password = "password";
        user.setEmail(personDto.getEmail());

        switch (personDto.getRole()) {
            case STUDENT:
                Set<Role> studentRoles = new HashSet<>();
                String studentUsername = usernameGenerator(personDto.getFirstName(), 2000, 9999);
                //Generates a new username if it already exists. Very unlikely but could happen.
                while (userRepository.existsByUsername(studentUsername)) {
                    studentUsername = usernameGenerator(personDto.getFirstName(), 2000, 9999);
                }
                user.setUsername(studentUsername);
                user.setEmail(personDto.getEmail());
                user.setPassword(encoder.encode(password));
                studentRoles.add(userRole);
                user.setRoles(studentRoles);
                userRepository.save(user);
                break;
            case TEACHER:
                Set<Role> teacherRoles = new HashSet<>();
                String teacherUsername = usernameGenerator(personDto.getFirstName(), 200, 999);
                //Generates a new username if it already exists. Very unlikely but could happen.
                while (userRepository.existsByUsername(teacherUsername)) {
                    teacherUsername = usernameGenerator(personDto.getFirstName(), 200, 999);
                }
                user.setUsername(teacherUsername);
                user.setPassword(encoder.encode(password));
                teacherRoles.add(modRole);
                teacherRoles.add(userRole);
                user.setRoles(teacherRoles);
                userRepository.save(user);
                break;
        }
        return user;
    }

    //Generates a username from first four characters of parameter name plus a random value from min/max
    public String usernameGenerator(String name, int min, int max) {
        String firstFourChars = "";
        Random random = new SecureRandom();
        if(name.length() > 4) {
            firstFourChars = name.substring(0, 4);
        }
        else {
            firstFourChars = name;
        }
        String randomNumberFromMinMax = String.valueOf(random.nextInt(max - min));
        return firstFourChars + randomNumberFromMinMax;
    }

    public void changePassword(String oldPassword , String password) {
        User user = userRepository.findByPassword(encoder.encode(oldPassword)).orElseThrow(() -> new DataNotFoundException("Password does not match Users password!"));
        if (encoder.matches(encoder.encode(oldPassword), user.getPassword())) {
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
        }
        else {
            throw new DataNotFoundException("Password did not match!");
        }
    }

    public boolean existsByUsername(SignupRequest signupRequest) {
        return userRepository.existsByUsername(signupRequest.getUsername());
    }

    public boolean existsByEmail(SignupRequest signupRequest) {
        return userRepository.existsByEmail(signupRequest.getUsername());
    }

    private Authentication authorizeFromLoginRequest(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

}
