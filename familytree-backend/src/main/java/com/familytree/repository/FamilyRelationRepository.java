package com.familytree.repository;

import com.familytree.entity.FamilyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamilyRelationRepository extends JpaRepository<FamilyRelation, Long> {
    List<FamilyRelation> findByMemberId(Long memberId);
    List<FamilyRelation> findByFamilyId(Long familyId);
}
