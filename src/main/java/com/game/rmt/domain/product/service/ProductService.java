package com.game.rmt.domain.product.service;

import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.dto.ProductDTO;
import com.game.rmt.domain.product.dto.ProductSearchFilter;
import com.game.rmt.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> getProducts(ProductSearchFilter searchFilter) {
        List<Product> productList = productRepository.findProducts(searchFilter);

        if (productList == null || productList.isEmpty()) {
            return null;
        }

        return convertProductDTOList(productList);
    }

    private List<ProductDTO> convertProductDTOList(List<Product> productList) {
        List<ProductDTO> productDTOList = null;

        for (Product product : productList) {
            productDTOList.add(new ProductDTO(product));
        }

        return productDTOList;
    }
}
