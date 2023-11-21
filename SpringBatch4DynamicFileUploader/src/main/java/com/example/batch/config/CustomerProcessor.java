package com.example.batch.config;

import com.example.batch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        var age = Integer.parseInt(customer.getAge());

        if (age >= 18) {
            return customer;
        }
        return null;
    }
}
