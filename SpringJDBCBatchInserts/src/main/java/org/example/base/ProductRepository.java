package org.example.base;

import java.util.List;

public interface ProductRepository {
    void saveAll(List<Product> products);
}
