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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    Seller seller;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    String name;
    Integer price;
}
