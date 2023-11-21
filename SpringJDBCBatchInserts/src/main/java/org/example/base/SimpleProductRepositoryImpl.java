package org.example.base;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class SimpleProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public SimpleProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveAll(List<Product> products) {
        for (Product product : products) {
            jdbcTemplate.update("INSERT INTO PRODUCT (TITLE, CREATED_TS, PRICE) " +
                            "VALUES (?, ?, ?)",
                    product.getTitle(),
                    Timestamp.valueOf(product.getCreatedTs()),
                    product.getPrice());
        }
    }
}
