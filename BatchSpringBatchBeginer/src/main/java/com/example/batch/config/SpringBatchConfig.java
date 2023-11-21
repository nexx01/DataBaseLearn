package com.example.batch.config;

import com.example.batch.entity.Customer;
import com.example.batch.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerRepository customerRepository;

    public FlatFileItemReader<Customer> reader() {
        FileSystemResource expectedResult = new FileSystemResource("EXPECTED_OUTPUT");
        FileSystemResource actualResult = new FileSystemResource("OUTPUT");

        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new PathResource("./BatchSpringBatchBeginer/src/main/resources/customers.csv"));
        itemReader.setResource(new PathResource("./src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
//        itemReader.setStrict(false);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

  @Bean
  public CustomerProcessor processor(){
      return new CustomerProcessor();
  }
  
  @Bean
  public RepositoryItemWriter<Customer> writer(){
      var repositoryItemWriter = new RepositoryItemWriter<Customer>();
      repositoryItemWriter.setRepository(customerRepository);
      repositoryItemWriter.setMethodName("save");
      return repositoryItemWriter;
  }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step")
                .<Customer,Customer>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importCustomer")
                .flow(step1())
//                .next(step1())
                .end().build();
    }
  
    private LineMapper<Customer> lineMapper() {
        var lineMapper = new DefaultLineMapper<Customer>();
        var lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "email", "gender", "contactNo", "country", "dob");
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public TaskExecutor taskExecutor(){
        var executor = new SimpleAsyncTaskExecutor();
        executor.setConcurrencyLimit(10);
        return executor;
    }
}
