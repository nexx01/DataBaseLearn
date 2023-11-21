package com.example.batch;

import com.example.batch.repository.CustomerRepository;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@ContextConfiguration(classes = BatchSpringBatchBeginerApplication.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BatchSpringBatchBeginerApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    CustomerRepository customerRepository;

    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }


    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("file.input", "TEST_INPUT");
        paramsBuilder.addString("file.output", "TEST_OUTPUT");
        paramsBuilder.addLong("sizeChunk", 10L);
        return paramsBuilder.toJobParameters();
    }

    @Test
    void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
//        FileSystemResource expectedResult = new FileSystemResource("./src/main/resources/customers.csv");
//        FileSystemResource actualResult = new FileSystemResource("./src/main/resources/customers.csv");

        var jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        var actualJobInstance = jobExecution.getJobInstance();
        jobExecution.getStepExecutions().forEach(d -> System.out.println(d.getJobParameters()));
        var actualExitStatus = jobExecution.getExitStatus();
        Thread.sleep(1000);
        var count = customerRepository.count();

        assertThat(actualJobInstance.getJobName(), is("importCustomer"));
        assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
        assertThat(count,is(25L));
//        AssertFile.assertFileEquals(expectedResult, actualResult);
    }
}
