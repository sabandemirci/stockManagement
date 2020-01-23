package com.keremoglu.controller;

import com.keremoglu.dao.RoleRepository;
import com.keremoglu.dao.UserRepository;
import com.keremoglu.orm.Role;
import com.keremoglu.orm.User;
import com.keremoglu.payload.request.LoginRequest;
import com.keremoglu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/user/v1")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    public ResponseEntity<?> queryAllUsers() {
        List lst = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User u : userList) {
            lst.add(u.toMap());
        }
        return new ResponseEntity(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody Map map) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetailsImpl) token.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUserName(userName).get();
        User user = null;
        if (map.get("id") != null) {
            user = userRepository.getOne(Long.parseLong(map.get("id").toString()));
            user.setUpdatedUser(currentUser);
            user.setUpdatedTimestamp(new Date());
        } else {
            user = new User();
            user.setCreatedTimestamp(new Date());
            user.setCreatedUser(currentUser);
            user.setUserName(map.get("userName").toString());
        }
        user.setFirstName(map.get("firstName") != null ? map.get("firstName").toString() : null);
        user.setLastName(map.get("lastName") != null ? map.get("lastName").toString() : null);
        user.setEmail(map.get("email") != null ? map.get("email").toString() : null);
        if (map.get("role") != null) {
            Role r = new Role();
            r.setCreatedTimestamp(new Date());
            Role.ERole erole = null;
            Object o = map.get("role");
            if ("ROLE_ADMIN".equals(o)) {
                erole = Role.ERole.ROLE_ADMIN;
            } else if ("ROLE_USER".equals(o)) {
                erole = Role.ERole.ROLE_USER;
            } else if ("ROLE_MODERATOR".equals(o)) {
                erole = Role.ERole.ROLE_MODERATOR;
            }
            r.setName(erole);
            Role role = roleRepository.findByName(erole).orElse(r);
            user.setRole(role);
        }
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> queryUser(@PathVariable("userId") Long userId) {
        User u = userRepository.findById(userId).get();
        return new ResponseEntity(u.toMap(), HttpStatus.OK);
    }


}
