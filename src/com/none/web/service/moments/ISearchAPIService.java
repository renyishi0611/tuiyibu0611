package com.none.web.service.moments;

import java.util.List;
import java.util.Map;

import com.none.web.po.BuPO;

public interface ISearchAPIService {

	List<BuPO> searchUserMatchField(String field) throws Exception;

	Map<String, Object> searchMomentsByUserId(String logonUserId, String searchUserId, Integer mCurrentPage,
			Integer mNumperPage, Integer cCouurentPage, Integer cNumperPage) throws Exception;
}
