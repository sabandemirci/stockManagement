package com.keremoglu.controller;

import com.keremoglu.dao.*;
import com.keremoglu.orm.*;
import com.keremoglu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/warehouseAction/v1")
public class WareHouseActionController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    WarehouseActionRepository warehouseActionRepository;
    @Autowired
    MaterialRepository materialRepository;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Map map) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetailsImpl) token.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUserName(userName).get();
        Warehouse warehouse = null;
        WarehouseAction warehouseAction = new WarehouseAction();
        warehouseAction.setCreatedTimestamp(new Date());
        warehouseAction.setCreatedUser(currentUser);
        if (map.get("warehouseId") != null) {
            warehouse = warehouseRepository.getOne(Long.parseLong(map.get("warehouseId").toString()));
            warehouseAction.setWarehouse(warehouse);
        }
        if (map.get("action") != null) {
            if (map.get("action").toString().equals("INPUT"))
                warehouseAction.setAction(WarehouseAction.EAction.INPUT);
            if (map.get("action").toString().equals("OUTPUT"))
                warehouseAction.setAction(WarehouseAction.EAction.OUTPUT);
        }
        if (map.get("supplier") != null) {
            warehouseAction.setSupplier(map.get("supplier").toString());
        }
        if (map.get("amount") != null) {
            warehouseAction.setAmount(Double.valueOf(map.get("amount").toString()));
        }
        if (map.get("material") != null) {
            Material material = materialRepository.getOne(Long.parseLong(map.get("material").toString()));
            warehouseAction.setMaterial(material);
        }
        warehouseActionRepository.save(warehouseAction);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/fetchWarehouseActionsByWarehouse/{warehouseId}")
    public ResponseEntity<?> fetchWarehouseActionsByWarehouse(@PathVariable("warehouseId") Long id) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = ((UserDetailsImpl) token.getPrincipal()).getUsername();
        User currentUser = userRepository.findByUserName(userName).get();
        Warehouse warehouse = warehouseRepository.getOne(id);
        List<WarehouseAction> lst = warehouseActionRepository.findByWarehouse(warehouse).get();
        List ret = new ArrayList();
        for (WarehouseAction w : lst) {
            ret.add(w.toMap());
        }
        return new ResponseEntity(ret, HttpStatus.OK);
    }


}
