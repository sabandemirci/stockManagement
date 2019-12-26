package com.keremoglu.controller;

import com.keremoglu.dao.UserRepository;
import com.keremoglu.dao.WarehouseRepository;
import com.keremoglu.dao.WarehouseUserRepository;
import com.keremoglu.orm.User;
import com.keremoglu.orm.Warehouse;
import com.keremoglu.orm.WarehouseUser;
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
@RequestMapping("api/warehouse/v1")
public class WareHouseController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    WarehouseUserRepository warehouseUserRepository;

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> queryAll() {
        List lst = new ArrayList<>();
        List<Warehouse> ret = warehouseRepository.findAll();
        for (Warehouse u : ret) {
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
        Warehouse entity = null;
        if (map.get("id") != null) {
            entity = warehouseRepository.getOne(Long.parseLong(map.get("id").toString()));
            entity.setUpdatedUser(currentUser);
            entity.setUpdatedTimestamp(new Date());
        } else {
            entity = new Warehouse();
            entity.setCreatedTimestamp(new Date());
            entity.setCreatedUser(currentUser);
            entity.setCode(map.get("code").toString());
        }
        entity.setName(map.get("name") != null ? map.get("name").toString() : null);
        entity.setAddress(map.get("address") != null ? map.get("address").toString() : null);
        warehouseRepository.save(entity);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/saveUsers", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveUsers(@RequestBody Map map) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetailsImpl) token.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUserName(userName).get();
        Warehouse entity = null;
        if (map.get("id") != null) {
            entity = warehouseRepository.getOne(Long.parseLong(map.get("id").toString()));
            entity.setUpdatedUser(currentUser);
            entity.setUpdatedTimestamp(new Date());
        }
        if (map.get("selectedUserList") != null) {
            List<Integer> userList = (List<Integer>) map.get("selectedUserList");
            for (Integer userId : userList) {
                boolean status = false;
                for (WarehouseUser wu : entity.getWarehouseUserList()) {
                    if (wu.getUser().getId().equals(userId.longValue())) {
                        status = true;
                        break;
                    }
                }
                if (!status) {
                    User user = userRepository.getOne(Long.parseLong(userId.toString()));
                    WarehouseUser warehouseUser = new WarehouseUser();
                    warehouseUser.setUser(user);
                    warehouseUser.setWarehouse(entity);
                    warehouseUser.setCreatedTimestamp(new Date());
                    warehouseUser.setCreatedUser(currentUser);
                    warehouseUserRepository.save(warehouseUser);
                }
            }
            List<WarehouseUser> deletedList = new ArrayList<>();
            for (WarehouseUser wu : entity.getWarehouseUserList()) {
                boolean status = false;
                for (Integer userId : userList) {
                    if (wu.getUser().getId().equals(userId.longValue())) {
                        status = true;
                        break;
                    }
                }
                if (!status) {
                    warehouseUserRepository.delete(wu);
                    deletedList.add(wu);
                }
            }
            entity.getWarehouseUserList().removeAll(deletedList);
        } else {
            for (WarehouseUser wu : entity.getWarehouseUserList()) {
                warehouseUserRepository.delete(wu);
            }
        }
        warehouseRepository.save(entity);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> query(@PathVariable("warehouseId") Long id) {
        Warehouse u = warehouseRepository.findById(id).get();
        Map map = u.toMap();
        map.put("wareHouseUserList", new ArrayList<>());
        for (WarehouseUser wu : u.getWarehouseUserList()) {
            ((ArrayList) map.get("wareHouseUserList")).add(wu.toMap());
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/fetchWarehousesByUser")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> query() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetailsImpl) token.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUserName(userName).get();
        List<Warehouse> lst = warehouseRepository.getWarehouseByUser(currentUser.getId());
        List ret = new ArrayList();
        for (Warehouse w : lst) {
            ret.add(w.toMap());
        }
        return new ResponseEntity(ret, HttpStatus.OK);
    }


}
