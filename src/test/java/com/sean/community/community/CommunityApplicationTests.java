package com.sean.community.community;

import com.sean.community.community.Controller.AlphaController;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
	}

	@Test
	public void testBeanDate(){
		SimpleDateFormat sdf = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(sdf.format(new Date()));
	}

	@Autowired
	private AlphaController alphaController;


}
