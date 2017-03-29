
package com;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
	public static void main(final String[] args) {
		new ClassPathXmlApplicationContext(new String[]{"classpath*:**/applicationContext.xml", "classpath*:**/services.xml"});
	}
}
