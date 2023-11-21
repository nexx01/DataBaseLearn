package org.example.base;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductService {

    private ProductRepository productRepository;
    private Random random;
    private Clock clock;

    public ProductService(ProductRepository productRepository, Random random, Clock clock) {
        this.productRepository = productRepository;
        this.random = random;
        this.clock = clock;
    }

    // constructor for the dependencies

    private List<Product> generate(int count) {
        final String[] titles = { "car", "plane", "house", "yacht" };
        final BigDecimal[] prices = {
          new BigDecimal("12483.12"),
          new BigDecimal("8539.99"),
          new BigDecimal("88894"),
          new BigDecimal("458694")
        };

        final List<Product> products = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setCreatedTs(LocalDateTime.now(clock));
            product.setPrice(prices[random.nextInt(4)]);
            product.setTitle(titles[random.nextInt(4)]);
            products.add(product);
        }
        return products;
    }

    @Transactional
    public long createProducts(int count) {
        List<Product> products = generate(count);
        long startTime = clock.millis();
        productRepository.saveAll(products);
        return clock.millis() - startTime;
    }
}
