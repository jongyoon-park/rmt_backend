package com.game.rmt.domain.product.dto.response;

import com.game.rmt.domain.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductDTO> productList;
}
