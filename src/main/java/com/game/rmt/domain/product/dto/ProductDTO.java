package com.game.rmt.domain.product.dto;

import com.game.rmt.domain.game.dto.GameDTO;
import com.game.rmt.domain.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private boolean isActivated;
    private GameDTO game;

    public ProductDTO(Product product) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.isActivated = product.isActivated();
        this.game = new GameDTO(product.getGame());
    }
}
