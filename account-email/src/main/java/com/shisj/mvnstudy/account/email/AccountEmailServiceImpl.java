package com.shisj.mvnstudy.account.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class AccountEmailServiceImpl implements AccountEmailService {
	
	private JavaMailSender javaMailSender;
	private String systemEmail;

	public void sendMail(String to, String subject, String htmlText) throws AccountEmailException {
		System.out.println(String.format("sendMail: [to] %s  [subject] %s [htmlText] %s" + to +"  ", to,subject,htmlText));
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public String getSystemEmail() {
		return systemEmail;
	}

	public void setSystemEmail(String systemEmail) {
		this.systemEmail = systemEmail;
	}

	
}
