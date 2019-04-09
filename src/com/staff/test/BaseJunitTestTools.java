package com.staff.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"classpath:spring/applicationContext-core.xml","classpath:spring/applicationContext-hibernate-dataSource.xml","classpath:spring/applicationContext-hibernate.xml", "classpath:spring/applicationContext-servlet.xml"}) 
@Transactional 
@WebAppConfiguration
public class BaseJunitTestTools {
	
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	MockHttpSession session;
	
	@Autowired
	MockHttpServletRequest request;
}
