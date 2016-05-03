package duson.java.solutionConf.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RestDemoService {
	
	@RequestMapping("/")
    String home() {
		return "";
    }
	
}
