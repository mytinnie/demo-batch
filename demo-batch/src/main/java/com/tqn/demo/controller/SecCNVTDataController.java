package com.tqn.demo.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tqn.demo.configuration.RedisCacheConfiguration;
import com.tqn.demo.configuration.WebConfig;
import com.tqn.demo.model.SecCNVTData;
import com.tqn.demo.repository.SecCNVTRepository;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Import({RedisCacheConfiguration.class, WebConfig.class})
@RequestMapping("/secCNVT")
public class SecCNVTDataController {
	private static final Log logger = LogFactory.getLog(SecCNVTDataController.class);
	@Autowired
	private SecCNVTRepository secCNVTRepository;

//	@CachePut(value = "customerPartNumber", key = "#customerPartNumber")
	@Cacheable(value="customerPartNumber", key = "#customerPartNumber")
	@RequestMapping(value = "/customerPartNumber/{customerPartNumber}", method = RequestMethod.GET)
	public Iterable<SecCNVTData> getSecCNVTByCustomerPartNumber(@PathVariable String customerPartNumber) {
		logger.info("query for customerPartNumber : " + customerPartNumber);
		return secCNVTRepository.findByCustomerPartNumber(customerPartNumber);
	}

//	@CrossOrigin(origins = "*")
//Note: CrossOrigin only works with newer Spring
	@RequestMapping(value = "/alldata", method = RequestMethod.GET)
	public Iterable<SecCNVTData> getAllData() {
		logger.info("query for all data");
		return secCNVTRepository.findAll();
	}


}
