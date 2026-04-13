package com.community.groupon.service;

import com.community.groupon.entity.Product;
import com.community.groupon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.selectList(null);
    }

    public Product findById(Long id) {
        return productRepository.selectById(id);
    }

    public Product save(Product product) {
        if (product.getId() != null) {
            productRepository.updateById(product);
        } else {
            productRepository.insert(product);
        }
        return product;
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public long count() {
        return productRepository.selectCount(null);
    }
}