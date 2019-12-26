package com.keremoglu.orm;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "WAREHOUSE")
public class Warehouse extends BaseEntity {

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "address")
    private String address;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "warehouse")
    private Set<WarehouseUser> warehouseUserList = new HashSet<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Map toMap() {
        Map m = new HashMap();
        m.put("id", getId());
        m.put("code", code);
        m.put("name", name);
        m.put("address", address);
        return m;
    }

    public Set<WarehouseUser> getWarehouseUserList() {
        return warehouseUserList;
    }

    public void setWarehouseUserList(Set<WarehouseUser> warehouseUserList) {
        this.warehouseUserList = warehouseUserList;
    }
}