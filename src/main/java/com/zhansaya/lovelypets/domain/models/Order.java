package com.zhansaya.lovelypets.domain.models;

import com.zhansaya.lovelypets.domain.enums.OrderStatus;
import com.zhansaya.lovelypets.domain.enums.OrganizationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @CreationTimestamp
    @Column(name = "order_date", nullable = false, updatable = false)
    Instant orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    OrderStatus orderStatus;

    @PrePersist
    public void prePersist() {
        if (orderStatus == null) {
            orderStatus = OrderStatus.PLACED;
        }
    }
}
