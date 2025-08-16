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
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    String city;
    String address;
    Boolean loyaltyMember;

    @PrePersist
    public void prePersist(){
        if (loyaltyMember == null) {
            loyaltyMember = false;
        }
    }
}
