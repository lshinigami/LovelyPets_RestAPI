package com.zhansaya.lovelypets.domain.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 100, unique = true)
    String name;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}
