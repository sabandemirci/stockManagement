package com.keremoglu.dao;

import com.keremoglu.orm.Warehouse;
import com.keremoglu.orm.WarehouseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseUserRepository extends JpaRepository<WarehouseUser, Long> {

}
