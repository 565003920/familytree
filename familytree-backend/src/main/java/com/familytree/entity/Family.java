package com.familytree.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "families")
@Where(clause = "deleted = 0")
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String originPlace;
    private Long adminId;
    private String description;
    private Integer memberCount;
    private Integer status;

    @Column(nullable = false)
    private Integer deleted = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
