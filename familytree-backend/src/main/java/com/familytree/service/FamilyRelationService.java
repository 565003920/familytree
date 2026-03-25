package com.familytree.service;

import com.familytree.entity.FamilyMember;
import com.familytree.entity.FamilyRelation;
import com.familytree.repository.FamilyMemberRepository;
import com.familytree.repository.FamilyRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FamilyRelationService {
    @Autowired
    private FamilyRelationRepository relationRepository;

    @Autowired
    private FamilyMemberRepository memberRepository;

    public FamilyRelation createRelation(Long familyId, Long memberId, Long relatedMemberId, String relationType) {
        FamilyRelation relation = new FamilyRelation();
        relation.setFamilyId(familyId);
        relation.setMemberId(memberId);
        relation.setRelatedMemberId(relatedMemberId);
        relation.setRelationType(relationType);
        relation.setCreatedAt(LocalDateTime.now());
        return relationRepository.save(relation);
    }

    public List<FamilyRelation> getRelationsByMember(Long memberId) {
        return relationRepository.findByMemberId(memberId);
    }

    public void deleteRelation(Long id) {
        relationRepository.deleteById(id);
    }

    public Map<String, Object> getTreeData(Long familyId) {
        List<FamilyMember> members = memberRepository.findByFamilyId(familyId);
        Map<String, Object> tree = new HashMap<>();
        tree.put("relations", members);
        return tree;
    }
}
