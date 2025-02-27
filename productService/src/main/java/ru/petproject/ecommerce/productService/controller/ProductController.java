package ru.petproject.ecommerce.productService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import ru.petproject.ecommerce.productService.dto.ProductDto;
import ru.petproject.ecommerce.productService.exceptions.ProductNotFoundException;
import ru.petproject.ecommerce.productService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> findAllProducts() {
        logger.info("Вывод всех товаров");
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        logger.info("Вывод товара с id: {}", id);
        return productService.findByIdProduct(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto, @RequestHeader("Authorization") String token) {
        logger.info("Создание товара");
        token = token.replace("Bearer ", "");
        return productService.createProduct(productDto, token);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDTO, @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        logger.info("Обновление товара с id: {}", id);
        return productService.updateProduct(id, productDTO, token);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Удаление товара с id: {}", id);
        token = token.replace("Bearer ", "");
        productService.deleteProduct(id, token);
    }
}
