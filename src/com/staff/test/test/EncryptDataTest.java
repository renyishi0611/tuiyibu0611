package com.staff.test.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * JUnit测试
 * @author 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localhost-dataSource.xml")
public class EncryptDataTest   {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Test
	public void TestEncryptData(){
		StringBuffer sb=new StringBuffer();
		sb.append("UPDATE t_user_app SET user_name=:userName where user_id='10tu' ");
		sb.append(" ; ");
		sb.append("UPDATE t_user_app SET user_name='11hh' where user_id='11hh'");
		
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("userName", "123");
		namedJdbcTemplate.update(sb.toString(), map);
		
//		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sb.toString(), "10tu","111");
//		for (Map<String, Object> map : queryForList) {
//			System.out.println(map.get("user_id"));
//		}
//		System.out.println(jdbcTemplate);
	}
	
	
}
