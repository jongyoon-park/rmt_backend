package com.game.rmt.domain.product.repository.custom;

import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.dto.ProductSearchFilter;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findProducts(ProductSearchFilter productSearchFilter);
    Product findProductById(Long productId);
    Product findProductByGameIdAndProductName(Long gameId, String productName);
}
