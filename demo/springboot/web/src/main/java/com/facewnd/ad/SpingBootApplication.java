package com.facewnd.ad;

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

import com.facewnd.ad.autoconfig.ConfigAutoConfiguration;
import com.facewnd.ad.modules.test.TestService;

@SpringBootApplication
@EnableCircuitBreaker
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SpingBootApplication extends SpringBootServletInitializer implements CommandLineRunner {
	@Autowired
	private TestService testService;
	
	private Logger logger = LogManager.getLogger(getClass());
	
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
		//List<Users> list = usersMapper.selectAll();
		//logger.debug(JSON.toJSONString(list));
		
		/*logger.info("info");
		logger.warn("warn");
		logger.error("error");*/
		
		//testService.test1();
		//testService.test("abc");
		
	}

}
