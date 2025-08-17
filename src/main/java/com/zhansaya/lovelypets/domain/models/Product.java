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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    BigDecimal price;

    @Column(name = "typo_flag", nullable = false)
    Boolean typoFlag = false;

    @Column(name = "typo_note", length = 500)
    String typoNote;
}
