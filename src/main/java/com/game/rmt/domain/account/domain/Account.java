package com.game.rmt.domain.account.domain;

import com.game.rmt.domain.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private Integer price;
    private LocalDate purchaseDate;
    private LocalDateTime createDate;
    private String note;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Account(Integer price, LocalDate purchaseDate, Product product) {
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.createDate = LocalDateTime.now();

        if (product != null) {
            changeProduct(product);
        }
    }

    public Account(Integer price, LocalDate purchaseDate, String note, Product product) {
        this.price = price;
        this.note = note;
        this.purchaseDate = purchaseDate;
        this.createDate = LocalDateTime.now();

        if (product != null) {
            changeProduct(product);
        }
    }

    private void changeProduct(Product product) {
        this.product = product;
        product.getAccounts().add(this);
    }
}
