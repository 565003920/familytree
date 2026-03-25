package com.familytree.repository;

import com.familytree.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    List<Family> findByAdminId(Long adminId);
}
