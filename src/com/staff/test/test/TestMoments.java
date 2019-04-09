package com.staff.test.test;

import org.junit.Test;

import com.google.gson.Gson;
import com.staff.test.BaseJunitTestTools;

/**
 * test moments module
 * @author qiusijing
 *
 */
public class TestMoments extends BaseJunitTestTools {

	private Gson gson = new Gson();

	@Test
	public void testCommentsList() {
		try {
			//List<CommentPO> list = iCommentsService.selectCommentsListByMomentsId(1);
			String json = gson.toJson(12);
			System.out.println("result =====" + json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
