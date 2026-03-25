package com.familytree.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "family_members")
@Where(clause = "deleted = 0")
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long familyId;
    private Long userId;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String generation;
    private Integer ranking;
    private String bio;

    private Long fatherId;
    private Long motherId;
    private Long spouseId;

    @Column(nullable = false)
    private Integer deleted = 0;

    private LocalDateTime createdAt;
}
