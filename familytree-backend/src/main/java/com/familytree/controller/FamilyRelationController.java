package com.familytree.controller;

import com.familytree.entity.FamilyRelation;
import com.familytree.service.FamilyRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FamilyRelationController {
    @Autowired
    private FamilyRelationService relationService;

    @PostMapping("/relations")
    public ResponseEntity<FamilyRelation> createRelation(@RequestBody Map<String, Object> request) {
        Long familyId = Long.valueOf(request.get("familyId").toString());
        Long memberId = Long.valueOf(request.get("memberId").toString());
        Long relatedMemberId = Long.valueOf(request.get("relatedMemberId").toString());
        String relationType = request.get("relationType").toString();
        return ResponseEntity.ok(relationService.createRelation(familyId, memberId, relatedMemberId, relationType));
    }

    @GetMapping("/relations/member/{memberId}")
    public ResponseEntity<List<FamilyRelation>> getRelationsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(relationService.getRelationsByMember(memberId));
    }

    @DeleteMapping("/relations/{id}")
    public ResponseEntity<Void> deleteRelation(@PathVariable Long id) {
        relationService.deleteRelation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/families/{familyId}/tree-data")
    public ResponseEntity<Map<String, Object>> getTreeData(@PathVariable Long familyId) {
        return ResponseEntity.ok(relationService.getTreeData(familyId));
    }
}
