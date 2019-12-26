package com.keremoglu.controller;

import com.keremoglu.dao.RoleRepository;
import com.keremoglu.dao.UserRepository;
import com.keremoglu.orm.Role;
import com.keremoglu.orm.User;
import com.keremoglu.payload.request.LoginRequest;
import com.keremoglu.payload.request.SignupRequest;
import com.keremoglu.payload.response.JwtResponse;
import com.keremoglu.payload.response.MessageResponse;
import com.keremoglu.security.jwt.JwtUtils;
import com.keremoglu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth/v1")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        AtomicReference<Role> role = new AtomicReference<>();


        strRoles.forEach(rx -> {
            Role r = new Role();
            r.setCreatedTimestamp(new Date());
            switch (rx) {
                case "admin":
                    r.setName(Role.ERole.ROLE_ADMIN);
                    Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                            .orElse(r);
                    role.set(adminRole);

                    break;
                case "mod":
                    r.setName(Role.ERole.ROLE_MODERATOR);
                    Role modRole = roleRepository.findByName(Role.ERole.ROLE_MODERATOR)
                            .orElse(r);
                    role.set(modRole);
                    break;
                default:
                    r.setName(Role.ERole.ROLE_USER);
                    Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                            .orElse(r);
                    role.set(userRole);
            }
        });

        user.setRole(role.get());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
