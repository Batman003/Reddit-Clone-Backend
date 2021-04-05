package com.reddit.application.service;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.reddit.application.common.CommonUtils;
import com.reddit.application.constant.RedditConstant;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class MailService {
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	MailContentBuilder mailContentBuilder;
	
	private final CommonUtils commonUtils;
	
	@Async
	public void sendMail(NotificationEmail notificationEmail) {
		
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			
			mimeMessageHelper.setFrom(commonUtils.getProperty("from.email"));
			mimeMessageHelper.setTo(notificationEmail.getRecipient());
			mimeMessageHelper.setSubject(notificationEmail.getSubject());
			mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()), true);
			
		};
		
		try {
			
			mailSender.send(messagePreparator);
			log.info("Activation Email Sent!");
			
		} catch (MailException e) {
			// TODO: handle exception
			log.error("Exception Occured when sending mail to "+notificationEmail.getRecipient());
			throw new SpringRedditException("Exception occured when sending mail to "+notificationEmail.getRecipient());
		}
	}
}
