package com.poluhin.ss.demo.repository;

import com.poluhin.ss.demo.domain.entity.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	Optional<RoleEntity> findByName(String name);
}
