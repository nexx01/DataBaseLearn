package com.example.batch.config;

import com.example.batch.entity.Customer;
import com.example.batch.repository.CustomerRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerWriter implements ItemWriter<Customer> {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        System.out.println("---> Thread name :- " + Thread.currentThread().getName());
        customerRepository.saveAll(items);
    }
}
