package com.example.batchinsertspringbootspringbatch;

import com.example.batchinsertspringbootspringbatch.base.SpringBatchConfig;
import com.example.batchinsertspringbootspringbatch.base.SpringBatchRetryConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
//@EnableTransactionManagement
//@EnableJpaRepositories

@Profile("spring")
public class BatchInsertSpringBootSpringBatchApplication  {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext();
		context.getEnvironment().addActiveProfile("spring");
//		context.register(SpringBatchConfig.class);
		context.refresh();

		var jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		var job = (Job) context.getBean("firstBatchJob");
		System.out.println("Starting the batch job");
		try {
			var execution = jobLauncher.run(job, new JobParameters());
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Job failed");
		}

	}

}
