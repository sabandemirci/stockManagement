package com.keremoglu.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "WAREHOUSE_ACTION")
public class WarehouseAction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnore
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonIgnore
    private Warehouse warehouse;

    @Basic
    @Column(name = "supplier")
    private String supplier;

    @Basic
    @Column(name = "unitPrice")
    private Double unitPrice;

    @Basic
    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private WarehouseAction.EAction action;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public EAction getAction() {
        return action;
    }

    public void setAction(EAction action) {
        this.action = action;
    }

    @Override
    public Map toMap() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Map map = new HashMap();
        map.put("material", material.getName());
        map.put("warehouse", warehouse.getName());
        map.put("supplier", supplier);
        map.put("action", action);
        map.put("amount", amount);
        map.put("createdTimestamp", formatter.format(getCreatedTimestamp()));
        return map;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public enum EAction {
        INPUT,
        OUTPUT
    }
}