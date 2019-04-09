package com.none.web.dao;

import org.springframework.stereotype.Repository;

import com.none.web.po.DictionaryPO;
import com.none.web.utils.BaseDao;

@Repository
public interface DictionaryDao extends BaseDao<DictionaryPO> {

	String getValueByKey(DictionaryPO po) throws Exception;
	
}
