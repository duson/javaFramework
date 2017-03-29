
package com.xxx;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SendMailThread extends Thread {

	private JavaMailSender mailSender;
	private SimpleMailMessage mailMessage;
	private StackTraceElement[] errorMsg;

	public SystemExceptionThread(StackTraceElement[] errorMsg, JavaMailSender mailSender, SimpleMailMessage mailMessage) {
		this.mailSender = mailSender;
		this.errorMsg = errorMsg;
		this.mailMessage = mailMessage;
	}

	@Override
	public void run() {
		if (null == errorMsg || errorMsg.length == 0) return;
		if (null == mailSender || null == mailMessage) return;

		try {
			mailMessage.setFrom(new InternetAddress(MimeUtility.encodeText("xxxx")));
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		mailMessage.setTo("xxx@xx.com");
		mailMessage.setSubject("");
		mailMessage.setText("");
		mailSender.send(mailMessage);
	}
}
