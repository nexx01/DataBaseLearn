package com.example.batch.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/jobs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    private final String TEMP_STORAGE = "C:\\Users\\V\\Desktop\\bath-files\\";

    @PostMapping
    public void importCsvToDbJob() {
        var jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping(path = "/importData")
    public void startBatch(@RequestParam("file") MultipartFile multipartFile) {

        //file ->path we don't know
        //copy the file to some storage in your VM
        //copy the file to DB: get the file path

        try {
            String originalFileName = multipartFile.getOriginalFilename();
//            File fileToImport = new File(originalFileName);
            File fileToImport = new File(TEMP_STORAGE+originalFileName);
            multipartFile.transferTo(fileToImport);
            var jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName",
//                            fileToImport.getAbsolutePath())
                            TEMP_STORAGE+originalFileName)
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            var execution = jobLauncher.run(job, jobParameters);

//            if (execution.getExitStatus().getExitCode()
//                    .equals(ExitStatus.COMPLETED)) {
//                  delete the file from the TEMP_STORAGE
//                Files.deleteIfExists(Paths.get(TEMP_STORAGE + originalFileName));
//            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
