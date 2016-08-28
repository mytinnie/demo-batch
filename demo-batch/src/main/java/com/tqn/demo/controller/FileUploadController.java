package com.tqn.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.tqn.demo.batch.configuration.LocalfilesToDestinationConfiguration;
import com.tqn.demo.batch.configuration.RemotefilesToDestinationConfiguration;
import com.tqn.demo.integration.configuration.FTPfilesToBatchConfiguration;

@Controller // Defines that this class is a spring bean
@Import({LocalfilesToDestinationConfiguration.class,RemotefilesToDestinationConfiguration.class,FTPfilesToBatchConfiguration.class})
public class FileUploadController {
	final static String FTPJOBNAME = "RemotefilesToFlatfilesJob";
	final static String UPLOADJOBNAME = "LocalfilesToDestinationJob";
	final static String INFILENAMEPARAM = "infilename";
	final static String OUTFILENAMEPARAM = "outfilename";
	final static String TIMEPARAM = "time";
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobRegistry jobRegistry;
//	@Autowired
//	private Job localfilesToDestinationJob;

	@Value("${local.in.file.path}")
	private String filePath;

    @RequestMapping(value="/ftp", method=RequestMethod.GET)
    public @ResponseBody String handleFTPUpload() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {
		JobParameters jobParameters = new JobParametersBuilder()
											.addLong(TIMEPARAM, System.currentTimeMillis())
											.toJobParameters();
		jobLauncher.run(jobRegistry.getJob(FTPJOBNAME), jobParameters);
        return "You successfully transfer files!";
    }
    
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
    
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                //BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(FILEPATH+name)));
            	File tempfile = new File(new URI(filePath+file.getOriginalFilename()));
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tempfile));
                stream.write(file.getBytes());
                stream.close();
System.out.println("getJobNames = " + jobRegistry.getJobNames());
				JobParameters jobParameters = new JobParametersBuilder()
													.addString(INFILENAMEPARAM, file.getOriginalFilename())
													.addString(OUTFILENAMEPARAM, file.getOriginalFilename())
													.addLong(TIMEPARAM, System.currentTimeMillis())
													.toJobParameters();
                jobLauncher.run(jobRegistry.getJob(UPLOADJOBNAME), jobParameters);
                tempfile.delete();
                return "You successfully uploaded " + file.getOriginalFilename() + "!";
            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename() + " because the file was empty.";
        }
    }
}
