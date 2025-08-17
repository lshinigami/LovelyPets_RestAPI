package com.zhansaya.lovelypets.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private User user;

    @Column(length = 100)
    String city;

    @Column(length = 200)
    String address;

    @Column(name = "loyalty_member", nullable = false)
    Boolean loyaltyMember;

    @Column(name = "discount_percent", nullable = false)
    Integer discountPercent = 0;

    @PrePersist
    public void prePersist(){
        if (loyaltyMember == null) {
            loyaltyMember = false;
        }
        if (discountPercent == null) {
            discountPercent = 0;
        }
    }
}
