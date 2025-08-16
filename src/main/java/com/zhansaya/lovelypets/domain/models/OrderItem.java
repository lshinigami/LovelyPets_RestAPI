package com.zhansaya.lovelypets.domain.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    Product product;

    Integer itemQuantity;
    BigDecimal totalPrice;
}
