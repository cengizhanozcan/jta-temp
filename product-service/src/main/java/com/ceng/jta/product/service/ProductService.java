package com.ceng.jta.product.service;

import com.ceng.jta.product.model.Product;
import com.ceng.jta.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }

    public Product get(String id) {
        return productRepository.findById(id).orElse(null);
    }
}
