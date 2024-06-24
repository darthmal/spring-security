package com.salam.spring_security.services.productService;

import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer id, Product product) {
//        Optional<Product> p = productRepository.findById(id);
//        if (p.isPresent()) {
//            Product p2 = p.get();
//            p2.setCategory(pro);
//        }
        Product productToUpdate = getProductById(id);
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setName(product.getName());
        productToUpdate.setStock(product.getStock());
        productToUpdate.setPrice(product.getPrice());
        return productRepository.save(productToUpdate);
    }


    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

}
