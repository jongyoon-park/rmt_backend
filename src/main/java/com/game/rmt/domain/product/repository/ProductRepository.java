package com.game.rmt.domain.product.repository;

import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
