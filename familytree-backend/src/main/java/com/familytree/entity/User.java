package com.familytree.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String phone;
    private String email;
    private String passwordHash;
    private String avatarUrl;
    private Integer status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
