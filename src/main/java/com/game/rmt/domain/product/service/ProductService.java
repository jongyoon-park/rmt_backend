package com.game.rmt.domain.product.service;

import com.game.rmt.domain.game.domain.Game;
import com.game.rmt.domain.game.service.GameService;
import com.game.rmt.domain.platform.service.PlatformService;
import com.game.rmt.domain.product.domain.Product;
import com.game.rmt.domain.product.dto.NewProductRequest;
import com.game.rmt.domain.product.dto.ProductDTO;
import com.game.rmt.domain.product.dto.ProductSearchFilter;
import com.game.rmt.domain.product.repository.ProductRepository;
import com.game.rmt.global.errorhandler.exception.BadRequestException;
import com.game.rmt.global.errorhandler.exception.ErrorCode;
import com.game.rmt.global.errorhandler.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final PlatformService platformService;
    private final GameService gameService;

    public List<ProductDTO> getProducts(ProductSearchFilter searchFilter) {
        List<Product> productList = productRepository.findProducts(searchFilter);

        if (productList == null || productList.isEmpty()) {
            return null;
        }

        return convertProductDTOList(productList);
    }

    public ProductDTO createProduct(NewProductRequest newProductRequest) {
        Game validGame = validateCreateProduct(newProductRequest);
        return saveProduct(new Product(newProductRequest.getProductName(), validGame));
    }

    public ProductDTO activateProduct(long productId) {
        Product product = validateActivateProduct(productId);
        product.updateActivated();
        return saveProduct(product);
    }

    public ProductDTO deactivateProduct(long productId) {
        Product product = validateDeactivateProduct(productId);
        product.updateUnActivated();
        return saveProduct(product);
    }

    public Product getProduct(long productId) {
        Product product = productRepository.findProductById(productId);

        if (product == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        return product;
    }

    private Product validateActivateProduct(long productId) {
        Product product = getProduct(productId);

        if (product.isActivated()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_UPDATE_PRODUCT);
        }

        return product;
    }

    private Product validateDeactivateProduct(long productId) {
        Product product = getProduct(productId);

        if (!product.isActivated()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST_UPDATE_PRODUCT);
        }

        return product;
    }

    private ProductDTO saveProduct(Product product) {
        return new ProductDTO(productRepository.save(product));
    }

    private Game validateCreateProduct(NewProductRequest newProductRequest) {
        isValidCreateRequest(newProductRequest);
        validatePlatform(newProductRequest.getPlatformId());
        Game findGame = validateGame(newProductRequest.getGameId());
        validateProductName(newProductRequest.getGameId(), newProductRequest.getProductName());

        return findGame;
    }

    private void validateProductName(long gameId, String productName) {
        Product product = productRepository.findProductByGameIdAndProductName(gameId, productName);

        if (product == null) {
            return;
        }

        throw new BadRequestException(ErrorCode.BAD_REQUEST_CREATE_PRODUCT);
    }

    private Game validateGame(long gameId) {
        return gameService.getGame(gameId);
    }

    private void validatePlatform(long platformId) {
        platformService.getPlatform(platformId);
    }

    private void isValidCreateRequest(NewProductRequest newProductRequest) {
        newProductRequest.isValidParam();
    }

    private List<ProductDTO> convertProductDTOList(List<Product> productList) {
        List<ProductDTO> productDTOList = null;

        for (Product product : productList) {
            productDTOList.add(new ProductDTO(product));
        }

        return productDTOList;
    }
}
