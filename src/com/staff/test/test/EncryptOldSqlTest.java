package com.staff.test.test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;

/**
 * 老数据加密处理
 * 
 * @author
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDataSource.xml")
public class EncryptOldSqlTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void excute() {
		System.out.println(new Date());
		
		// list.add("t_logs,id,user_id");
		// list.add("t_user_app,user_id,i_d_card");
		// list.add("rgt_bookstat,BDetail_Sn,BDetail_staffemail");
		// list.add("t_share,id,userId");
		List<String> list = new ArrayList<String>();
		// list.add("表名,id,被加密列名");
		list.add("t_user_app,user_id,user_id");
		list.add("t_user_app,user_id,staffId");
		list.add("t_comments,id,userId");
		list.add("t_like,id,userId");
		list.add("rgt_bookstat,BDetail_Sn,BDetail_staffNo");
		list.add("rgt_bookrecommend,book_sn,StaffNo");
		list.add("t_upload_files,id,userId");
		list.add("t_ca_user_reg,id,user_id");
		list.add("t_ca_user_reg,id,staff_id");
		list.add("t_recentsearches,hisId,userId");
		list.add("t_reminder_calendar,id,userId");
		list.add("t_activity_appuser,id,user_id");
		list.add("t_survey_record,id,user_id");
		list.add("t_recruitment_push,id,user_id");
		list.add("t_appuser_grade,grade_id,user_id");
		list.add("t_answer_record,id,user_id");
		list.add("t_fleamarket_contacts,id,userId");
		list.add("t_fleamarket_goods,id,createUserId");
		list.add("t_fleamarket_goods,id,updateUserId");
		list.add("t_fleamarket_goodscomment,id,userId");

		for (String listStr : list) {
			String[] split = listStr.split(",");
			String tableName = split[0];// 表名
			final String idName = split[1];// id字段名
			final String col = split[2];// 要加密字段名
			try {
				String sql = "select " + idName + "," + col + " from " + tableName;
				final List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
				String updatesql = "update " + tableName + " set " + col + "=? where " + idName + "=?";
				// 执行批量sql 处理多次插入操作
				jdbcTemplate.batchUpdate(updatesql, new BatchPreparedStatementSetter() {
					@Override
					// 执行次数
					public int getBatchSize() {
						return queryForList.size();
					}
					@Override
					// 执行参数
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Object object = queryForList.get(i).get(col);
						String value = null;
						if (object == null) {
							value = null;
						} else{
							value = AESUtil.encrypt(object.toString(), AppConstants.QR_CODER_AES_STATIC_KEY);
						}
						ps.setString(1, value);
						ps.setString(2, queryForList.get(i).get(idName).toString());
					}
				});
			} catch (Exception e) {
				System.out.println(e.getMessage()+e.getLocalizedMessage());
//				e.printStackTrace();
				continue;
			}
		}
		System.out.println(new Date());
	}
}
