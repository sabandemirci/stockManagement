package com.keremoglu.dao;

import com.keremoglu.orm.Material;
import com.keremoglu.orm.User;
import com.keremoglu.orm.Warehouse;
import com.keremoglu.orm.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByName(String name);

    Optional<Warehouse> findByCode(String code);

    @Query(
            value = "select * from warehouse w where w.id in (\n" +
                    "select wu.warehouse_id from warehouse_user wu where wu.user_id=?1\n" +
                    ")",
            nativeQuery = true)
    List<Warehouse> getWarehouseByUser(Long userId);
}
