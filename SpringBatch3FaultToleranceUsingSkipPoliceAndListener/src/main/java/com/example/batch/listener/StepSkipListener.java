package com.example.batch.listener;

import com.example.batch.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;

public class StepSkipListener implements SkipListener<Customer,Number> {


     //StepExecution

    Logger log = LoggerFactory.getLogger(StepSkipListener.class);

    @Override //item reader
    public void onSkipInRead(Throwable t) {
        log.info("A failure on read {}" , t.getMessage());
    }

    @Override  //item writer
    public void onSkipInWrite(Number item, Throwable t) {
        log.info("A failure on write {}, {}" , t.getMessage(),item);
    }

    @SneakyThrows
    @Override  //item process
    public void onSkipInProcess(Customer item, Throwable t) {
        log.info("Item {} was skipped due to the exception {}",
                new ObjectMapper().writeValueAsString(item),
                t.getMessage());
    }
}
