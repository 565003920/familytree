package com.familytree.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "albums")
@Where(clause = "deleted = 0")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long familyId;
    private String name;
    private String description;

    @Column(nullable = false)
    private Integer deleted = 0;

    private LocalDateTime createdAt;
}
