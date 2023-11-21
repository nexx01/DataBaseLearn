package org.example.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.time.Clock;
import java.util.Random;

@Configuration
public class AppConfig {

    @Bean
    public ProductService simpleProductService(SimpleProductRepositoryImpl simpleProductRepositoryImpl) {
      return new ProductService(simpleProductRepositoryImpl, new Random(), Clock.systemUTC());
    }

    @Bean
    public ProductService batchProductService(BatchProductRepositoryImpl batchProductRepositoryImpl) {
        return new ProductService(batchProductRepositoryImpl, new Random(), Clock.systemUTC());
    }

//    @Bean
//    public JdbcTemplate jdbcTemplate() throws SQLException {
//        final JdbcTemplate template = new JdbcTemplate();
//        template.setDataSource(dataSource());
//        template.afterPropertiesSet();
//        return template;
//    }
}