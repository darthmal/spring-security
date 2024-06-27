package com.salam.spring_security.controller.product;

import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.services.productService.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        super();
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id) {
        return new ResponseEntity<Product>(productService.getProductById(id), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return new ResponseEntity<Product>(productService.addProduct(product), HttpStatus.ACCEPTED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
        return new ResponseEntity<>(productService.updateProduct(id, product), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

}
