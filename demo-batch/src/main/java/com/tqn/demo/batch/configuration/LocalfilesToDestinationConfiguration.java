package com.tqn.demo.batch.configuration;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import com.tqn.demo.configuration.MyInfrastructureConfiguration;
import com.tqn.demo.configuration.RepositoryConfiguration;
import com.tqn.demo.model.SecCNVTData;
import com.tqn.demo.model.SecCNVTFieldSetMapper;
import com.tqn.demo.repository.SecCNVTRepository;

//Note: Comment out the @Configuration and @Import to disable this configuration
@Configuration
@Import({ MyInfrastructureConfiguration.class, RepositoryConfiguration.class })
public class LocalfilesToDestinationConfiguration {
	private static final Log logger = LogFactory.getLog(LocalfilesToDestinationConfiguration.class);
	final static String INFILENAMEPARAM = "infilename";
	final static String OUTFILENAMEPARAM = "outfilename";
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private SecCNVTRepository secCNVTRepository;
	
	@Value("${local.in.file.path}")
	private String inFilePath;
	@Value("${local.out.file.path}")
	private String outFilePath;

	@Bean
	public Job localfilesToDestinationJob(Step localFilesStep) throws Exception {
		logger.info("job: LocalfilesToDestinationJob");
		return jobBuilderFactory.get("LocalfilesToDestinationJob")
				.incrementer(new RunIdIncrementer())
	    		.start(localFilesStep)
	    		.build();
	}
	
	@Bean
	public Step localFilesStep(MultiResourceItemReader<SecCNVTData> multifilereader, FlatFileItemWriter<SecCNVTData> filewriter) throws IOException {
		logger.info("step: processOneLocalFilesStep");
		return stepBuilderFactory.get("LocalFilesStep")
				.<SecCNVTData,SecCNVTData>chunk(10)
	    		.reader(multifilereader)
	    		.processor(processor())
	    		.writer(dbwriter())
	    		.build();
	}

	@Bean
	@StepScope
	public MultiResourceItemReader<SecCNVTData> multifilereader(@Value("#{jobParameters[infilename]}") String filename) throws IOException {
		logger.info("multifilereader infilename: " + filename);
		MultiResourceItemReader<SecCNVTData> reader = new MultiResourceItemReader<SecCNVTData>();
		reader.setDelegate(filereader());
		reader.setResources(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(inFilePath+filename));
		return reader;
	}

	@Bean
	public FlatFileItemReader<SecCNVTData> filereader() {
		logger.info("filereader");
		FlatFileItemReader<SecCNVTData> itemReader = new FlatFileItemReader<SecCNVTData>();
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	
	@Bean
	public LineMapper<SecCNVTData> lineMapper() {
		logger.info("lineMapper");
		DefaultLineMapper<SecCNVTData> lineMapper = new DefaultLineMapper<SecCNVTData>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"transactionDate","transactionTime","transactionCode","customerPartNumber","internalPlantCode","zeroQuantityIndicator","baseUOM"});
		tokenizer.setDelimiter(org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_TAB);
		//use lineTokenizer.setIncludedFields for particular fields otherwise all fields are included
		lineMapper.setLineTokenizer(tokenizer);		
		lineMapper.setFieldSetMapper(new SecCNVTFieldSetMapper<SecCNVTData>());
		
		return lineMapper;
	}
	
	@Bean
	public ItemProcessor<SecCNVTData,SecCNVTData> processor() {
		logger.info("processor");
		return new ItemProcessor<SecCNVTData,SecCNVTData>() {
			public SecCNVTData process(SecCNVTData item) throws Exception {
				logger.info("process one SecCNVTData item " + item.getTransactionDateTime());
				// transform item if needed here
				return item;
			}
		};		 
	}
	
	// Use this writer to write to local file
	@Bean
	@StepScope
	public FlatFileItemWriter<SecCNVTData> filewriter(@Value("#{jobParameters[outfilename]}") String filename) throws IOException {
		logger.info("filewriter - outfilename: " + filename);
		FlatFileItemWriter<SecCNVTData> itemWriter = new FlatFileItemWriter<SecCNVTData>();
		DelimitedLineAggregator<SecCNVTData> lineAggregator = new DelimitedLineAggregator<SecCNVTData>();
		lineAggregator.setDelimiter(org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA);
		BeanWrapperFieldExtractor<SecCNVTData> fieldExtractor = new BeanWrapperFieldExtractor<SecCNVTData>();
		fieldExtractor.setNames(new String[]{"transactionDateTime","customerPartNumber","internalPlantCode","baseUOM"});
		lineAggregator.setFieldExtractor(fieldExtractor);
				
		itemWriter.setLineAggregator(lineAggregator);
		itemWriter.setResource(resourceLoader.getResource(outFilePath+filename));

		return itemWriter;
	}
	
	// Use this writer to write to database
	@Bean
	@StepScope
	public RepositoryItemWriter<SecCNVTData> dbwriter() throws IOException {
		logger.info("dbwriter");
		RepositoryItemWriter<SecCNVTData> itemWriter = new RepositoryItemWriter<SecCNVTData>();
		itemWriter.setRepository(secCNVTRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}
}
