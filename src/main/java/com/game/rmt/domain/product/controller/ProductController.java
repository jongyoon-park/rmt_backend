package com.game.rmt.domain.product.controller;

import com.game.rmt.domain.product.dto.ProductListResponse;
import com.game.rmt.domain.product.dto.ProductSearchFilter;
import com.game.rmt.domain.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("rmt/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ProductListResponse getProducts(@RequestBody ProductSearchFilter searchFilter) {
        return new ProductListResponse(productService.getProducts(searchFilter));
    }
}
