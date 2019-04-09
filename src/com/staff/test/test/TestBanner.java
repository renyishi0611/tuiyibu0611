package com.staff.test.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.gson.Gson;
import com.none.core.exception.ValidateException;
import com.none.web.dao.UserAppDao;
import com.none.web.model.TMainBanner;
import com.none.web.po.MainBannerPO;
import com.none.web.po.UserAppPO;
import com.none.web.service.banner.IMainBannerService;
import com.none.web.service.user.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/applicationContext-core.xml" })
public class TestBanner {

	@Autowired
	private IMainBannerService iMainBannerService;

	@Autowired
	private UserAppDao userAppDao;

	@Autowired
	private IUserService userService;

	private Gson gson = new Gson();

	@Test
	public void testFindById() {
		try {
			TMainBanner tMainBanner = iMainBannerService.findById(2);
			String json = gson.toJson(tMainBanner);
			System.out.println("result =====" + json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPath() {
		try {
			MainBannerPO banner = iMainBannerService.getPath();
			String json = gson.toJson(banner);
			System.out.println("result =====" + json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void geUsernameAndImgAndIsVIP() {
		try {
			Map<String, Object> map = userService.getUserInfoByUserIdAndPlatform("4", "cms");
			System.out.println("========" + map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getUserInfoByUserIdAndPlatform() throws Exception {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("staffId", "F9CC5BB163DF1A619C50D149AC73D759");
		list.add(map);

		userAppDao.updateUserAppStatus(list);

		// try {
		// List<UserAppPO> list = userAppDao.getUserInfoByUDID("23");
		// System.out.println("list========" + list);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
