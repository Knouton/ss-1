package com.poluhin.ss.demo.repository;

import com.poluhin.ss.demo.domain.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceObjectRepository extends JpaRepository<ResourceObjectEntity, Integer> {
}
