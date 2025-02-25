package ru.petproject.ecommerce.productService.controller;

import org.springframework.http.HttpStatus;
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
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return productService.findByIdProduct(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@RequestHeader("userLog") String userLog, @PathVariable Long id, @RequestBody ProductDto productDTO) {
        return productService.updateProduct(id, productDTO, userLog);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto, @RequestParam String userLog) {
        return productService.createProduct(productDto, userLog);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id, @RequestParam String userLog) {
        productService.deleteProduct(id, userLog);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException ex) {
        return ex.getMessage();
    }
}
