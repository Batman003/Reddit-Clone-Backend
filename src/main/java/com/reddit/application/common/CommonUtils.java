package com.reddit.application.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.reddit.application.service.MailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonUtils {

	@Autowired
	private Properties prop;

	@PostConstruct
	public void init() {
		prop = new Properties();
		try {
			prop.load(CommonUtils.class.getClassLoader().getResourceAsStream("reddit_cred.properties"));
			System.out.println("keyyyyyyyyyyyyyyyyy" + prop.toString());
		} catch (IOException e) {
			log.error("Exception Occured while loading properties");
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {

		String value = "";
		try {
			value = prop.getProperty(key);

		} catch (Exception e) {
			log.error("Exception Occured while loading properties");
			e.printStackTrace();
		}
		return value;
	}
	
	public static String getStackTrace(Exception e) {
		StringWriter stack = new StringWriter();
		e.printStackTrace(new PrintWriter(stack));
		return stack.toString();
	}
}
