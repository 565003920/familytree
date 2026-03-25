package com.familytree.repository;

import com.familytree.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findByFamilyId(Long familyId);
}
