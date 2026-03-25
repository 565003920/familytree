package com.familytree.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "family_relations")
public class FamilyRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long familyId;
    private Long memberId;
    private Long relatedMemberId;
    private String relationType;
    private LocalDateTime createdAt;
}
