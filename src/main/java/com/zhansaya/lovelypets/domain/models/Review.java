package com.zhansaya.lovelypets.domain.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    Product product;

    Integer rating;
    String commentText;
    Timestamp createdAt;
}
