package com.example.batch.config;

import com.example.batch.entity.Customer;
import com.example.batch.listener.StepSkipListener;
import com.example.batch.partition.ColumnRangePartition;
import com.example.batch.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerWriter customerWriter;

    public FlatFileItemReader<Customer> reader() {

        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new PathResource("./SpringBatch3FaultToleranceUsingSkipPoliceAndListener/src/main/resources/customers_broken.csv"));
//        itemReader.setResource(new PathResource("./src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
//        itemReader.setStrict(false);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }


    @Bean
    public ColumnRangePartition partitioner() {
        return new ColumnRangePartition();
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob","age");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }
//    @Bean
//    public PartitionHandler partitionHandler() {
//        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
//        taskExecutorPartitionHandler.setGridSize(4);
//        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
//        taskExecutorPartitionHandler.setStep(slaveStep());
//        return taskExecutorPartitionHandler;
//    }

    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep")
                .<Customer, Customer>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(customerWriter)
                .faultTolerant()
                .skipLimit(100)
                .listener(skipListener())
                .skipPolicy(skipPolicy())
               // .skip(NumberFormatException.class)
                //.noSkip(IllegalArgumentException.class)
                .build();
    }

    @Bean
    public SkipListener skipListener(){
        return new StepSkipListener();
    }

//    @Bean
//    public Step masterStep() {
//        return stepBuilderFactory.get("masterSTep").
//                partitioner(slaveStep().getName(), partitioner())
//                .partitionHandler(partitionHandler())
//                .build();
//    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importCustomers")
                .flow(slaveStep()).end().build();

    }
    
    @Bean
    public SkipPolicy skipPolicy(){
        return new ExceptionSkipPolicy();
    }

//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setMaxPoolSize(4);
//        taskExecutor.setCorePoolSize(4);
//        taskExecutor.setQueueCapacity(4);
//        return taskExecutor;
//    }
}