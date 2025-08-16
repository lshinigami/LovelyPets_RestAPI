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
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    Boolean isActive;
    String responsibleFor;
}
