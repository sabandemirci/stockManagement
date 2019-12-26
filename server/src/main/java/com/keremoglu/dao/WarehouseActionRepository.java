package com.keremoglu.dao;

import com.keremoglu.orm.Warehouse;
import com.keremoglu.orm.WarehouseAction;
import com.keremoglu.orm.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseActionRepository extends JpaRepository<WarehouseAction, Long> {
    Optional<List<WarehouseAction>> findByWarehouse(Warehouse warehouse);
}
