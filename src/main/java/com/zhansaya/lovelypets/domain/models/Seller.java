package com.zhansaya.lovelypets.domain.models;

import com.zhansaya.lovelypets.domain.enums.OrganizationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @Column(name = "organization_name", nullable = false, length = 50)
    String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false, length = 20)
    OrganizationType organizationType;

    @Column(name = "iin", length = 12)
    String iin;

    @Column(name = "bin", length = 12)
    String bin;

}
