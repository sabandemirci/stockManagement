package com.keremoglu.controller;

import com.keremoglu.dao.MaterialRepository;
import com.keremoglu.dao.RoleRepository;
import com.keremoglu.dao.UserRepository;
import com.keremoglu.orm.Material;
import com.keremoglu.orm.Role;
import com.keremoglu.orm.User;
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
@RequestMapping("api/material/v1")
public class MaterialController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MaterialRepository materialRepository;

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> queryAll() {
        List lst = new ArrayList<>();
        List<Material> ret = materialRepository.findAll();
        for (Material u : ret) {
            lst.add(u.toMap());
        }
        return new ResponseEntity(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody Map map) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetailsImpl) token.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUserName(userName).get();
        Material entity = null;
        if (map.get("id") != null) {
            entity = materialRepository.getOne(Long.parseLong(map.get("id").toString()));
            entity.setUpdatedUser(currentUser);
            entity.setUpdatedTimestamp(new Date());
        } else {
            entity = new Material();
            entity.setCreatedTimestamp(new Date());
            entity.setCreatedUser(currentUser);
        }
        entity.setCode(map.get("code") != null ? map.get("code").toString() : null);
        entity.setName(map.get("name") != null ? map.get("name").toString() : null);
        entity.setMinimumAmount(map.get("minimumAmount") != null ? Integer.parseInt(map.get("minimumAmount").toString()) : null);
        materialRepository.save(entity);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/material/{materialId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> query(@PathVariable("materialId") Long id) {
        Material u = materialRepository.findById(id).get();
        return new ResponseEntity(u.toMap(), HttpStatus.OK);
    }

}
