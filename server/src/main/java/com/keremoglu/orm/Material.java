package com.keremoglu.orm;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "MATERIAL")
public class Material extends BaseEntity {

    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "minimum_amount")
    private Integer minimumAmount;

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

    public Integer getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Integer minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    @Override
    public Map toMap() {
        Map m = new HashMap();
        m.put("id", getId());
        m.put("code", code);
        m.put("name", name);
        m.put("minimumAmount", minimumAmount);
        return m;
    }
}