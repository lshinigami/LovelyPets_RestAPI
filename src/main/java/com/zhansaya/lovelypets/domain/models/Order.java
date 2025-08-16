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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    Timestamp orderDate;
}
