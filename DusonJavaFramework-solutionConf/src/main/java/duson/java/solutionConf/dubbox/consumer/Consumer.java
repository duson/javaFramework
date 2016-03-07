package duson.java.solutionConf.dubbox.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import duson.java.solutionConf.dubbox.service.DemoService;

public class Consumer {
  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext(new String[] {"consumer.xml"});
    context.start();

    DemoService demoService = (DemoService) context.getBean("testService");

    String str = demoService.sayHello("张三");

    System.out.println(str);
  }
}
