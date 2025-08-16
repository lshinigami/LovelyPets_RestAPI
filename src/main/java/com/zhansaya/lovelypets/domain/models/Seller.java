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
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    String organizationName;
    String organizationType;
    Long iin;
    Long bin;

}
