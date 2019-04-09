package com.none.web.service.banner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.none.web.model.TMainBanner;
import com.none.web.po.MainBannerPO;

public interface IMainBannerService {

	String uploadBanner(MultipartFile multipartFile) throws Exception;

	TMainBanner saveMainBanner(TMainBanner bannerObj, HttpServletRequest request) throws Exception;

	List<TMainBanner> showMainBannerByCms() throws Exception;

	List<TMainBanner> showMainBannerByApp() throws Exception;

	int deleteByIds(List<Integer> ids) throws Exception;

	List<TMainBanner> getAllBanner() throws Exception;

	int save(TMainBanner tMainBanner) throws Exception;

	int update(TMainBanner tMainBanner) throws Exception;

	TMainBanner findById(Integer id) throws Exception;

	TMainBanner findByGroupAndOrder(String bannerGroup, String bannerOrder) throws Exception;

	MainBannerPO getPath() throws Exception;

}