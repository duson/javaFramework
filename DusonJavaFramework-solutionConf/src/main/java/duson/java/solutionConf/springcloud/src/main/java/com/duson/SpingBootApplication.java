package com.facewnd.ad;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableCircuitBreaker
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SpingBootApplication extends SpringBootServletInitializer implements CommandLineRunner {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private AdDictionaryMapper dictionaryMapper;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpingBootApplication.class);
	}
	
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(SpingBootApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

	}
	
}
