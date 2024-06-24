package com.salam.spring_security.services.productService;

import com.salam.spring_security.models.product.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    Product getProductById(Integer id);
    Product addProduct(Product product);
    Product updateProduct(Integer id, Product product);
    void deleteProduct(Integer id);
}
