package com.keremoglu.dao;

import com.keremoglu.orm.Material;
import com.keremoglu.orm.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByName(String name);

    Optional<Material> findByCode(String code);
}
