package com.game.rmt.domain.product.controller;

import com.game.rmt.domain.product.dto.request.NewProductRequest;
import com.game.rmt.domain.product.dto.ProductDTO;
import com.game.rmt.domain.product.dto.response.ProductListResponse;
import com.game.rmt.domain.product.dto.request.ProductSearchFilter;
import com.game.rmt.domain.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("rmt/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ProductListResponse getProducts(@RequestBody ProductSearchFilter searchFilter) {
        return new ProductListResponse(productService.getProducts(searchFilter));
    }

    @PostMapping("")
    public ProductDTO createProduct(@RequestBody NewProductRequest newProductRequest) {
        return productService.createProduct(newProductRequest);
    }

    @PostMapping("/activated/{productId}")
    public ProductDTO activateProduct(@PathVariable("productId") long productId) {
        return productService.activateProduct(productId);
    }

    @PostMapping("/unactivated/{productId}")
    public ProductDTO deactivateProduct(@PathVariable("productId") long productId) {
        return productService.deactivateProduct(productId);
    }
}
