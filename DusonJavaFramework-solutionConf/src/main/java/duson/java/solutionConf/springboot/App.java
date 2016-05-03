package duson.java.solutionConf.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource({"classpath:applicationContext.xml"})
public class App extends WebMvcConfigurerAdapter
{
    public static void main( String[] args )
    {
    	SpringApplication.run(RestDemoService.class, args); // 服务类
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new UserSecurityInterceptor());
    }
}
