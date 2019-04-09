package com.none.web.service.exhibition.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.none.core.common.utils.DateUtil;
import com.none.core.model.Pager;
import com.none.web.dao.ILivePollDao;
import com.none.web.model.LivePoll;
import com.none.web.model.PollAnswer;
import com.none.web.model.PollResult;
import com.none.web.model.StatusEnum;
import com.none.web.po.UserAppPO;
import com.none.web.service.exhibition.ILivePollAPIService;
import com.none.web.service.user.IUserService;

@Service
public class LivePollAPIServiceImpl implements ILivePollAPIService {

	public static Logger logger = Logger.getLogger(LivePollAPIServiceImpl.class);

	@Autowired
	private ILivePollDao livePollDaoImpl;

	@Autowired
	private IUserService userService;

	@Override
	public List<Map<String, Object>> queryLivePoll(Pager pager, String platform, String logonUserId) throws Exception {

		List<Map<String, Object>> queryList = livePollDaoImpl.queryLivePoll(pager, platform);
		if (null == queryList || queryList.size() == 0) {
			logger.info("the result is null, please query again");
			return null;
		}

		for (Map<String, Object> map : queryList) {
			getLivePollDetailInfo(map, platform, logonUserId);
		}

		return queryList;
	}

	@Override
	public List<Map<String, Object>> queryPollAnswer(int livePollId) {
		return livePollDaoImpl.queryPollAnswer(livePollId);
	}

	@Override
	public List<Map<String, Object>> queryPollResult(Pager pager, int livePollId) {
		return livePollDaoImpl.queryPollResult(livePollId, pager);
	}

	@Override
	public Map<String, Object> queryLivePollByLivePollId(int livePollId, String platform, String logonUserId)
			throws Exception {
		Map<String, Object> queryMap = livePollDaoImpl.queryLivePollById(livePollId);
		if (null == queryMap) {
			return null;
		}
		getLivePollDetailInfo(queryMap, platform, logonUserId);
		return queryMap;
	}

	private void getLivePollDetailInfo(Map<String, Object> map, String platform, String logonUserId) throws Exception {
		map.put("submitUserInfo",
				userService.getUserInfoByUserIdAndPlatform((String) map.get("submitUserId"), platform));
		map.put("answer", queryPollAnswer((Integer) map.get("livePollId")));
		map.put("submitTime", null == map.get("submitTime") ? null
				: DateUtil.staffTime(((Timestamp) map.get("submitTime")).toString()));
		map.putAll(totalPoll((Integer) map.get("livePollId")));
		map.put("isAnswer", checkLogonUserIsAnswer((Integer) map.get("livePollId"), logonUserId));
		map.put("status", StatusEnum.getName(((Integer) map.get("status")).intValue()));
		if (StringUtils.equals("cms", platform)) {
			map.put("userGroupInfo", queryUserGroupInfo(logonUserId));
		}

	}

	@Override
	public Map<String, Object> totalPoll(int livePollId) {
		return livePollDaoImpl.queryTotalPoll(livePollId);
	}

	public Integer checkLogonUserIsAnswer(int livePollId, String logonUserId) {
		Map<String, Object> result = null;
		try {
			result = livePollDaoImpl.checkLogonUserIsAnswer(livePollId, logonUserId);
		} catch (Exception e) {
			logger.error("no get query data, please check", e);
			return 0;
		}

		if (null != result && result.size() > 0) {
			return (Integer) result.get("isAnswer");
		}

		logger.info("any person is pollen answer");
		return 0;
	}

	@Override
	public int insertLivePoll(LivePoll livePoll) {
		return livePollDaoImpl.insertLivePoll(livePoll);
	}

	@Override
	public int updateLivePoll(LivePoll livePoll) {
		return livePollDaoImpl.updateLivePoll(livePoll);
	}

	@Override
	public int insertPollAnswer(int key, List<PollAnswer> pollAnswerList) {

		return livePollDaoImpl.insertPollAnswer(key, pollAnswerList);
	}

	@Transactional
	@Override
	public int updatePollAnswer(PollAnswer pollAnswer, UserAppPO userAppPO) {

		logger.info("start poll the answer.....");
		int polling = livePollDaoImpl.updatePollAnswer(pollAnswer);

		PollResult pollResult = new PollResult();
		pollResult.setPollAnswerId(pollAnswer.getId());
		pollResult.setLive_pollId(pollAnswer.getPollId());
		pollResult.setUserId(userAppPO.getUserId());
		pollResult.setUserName(userAppPO.getUserName());

		logger.info("record the detail info...");
		if (0 < polling) {
			polling = polling + livePollDaoImpl.insertPollResult(pollResult);
		}

		return polling;
	}

	@Transactional
	@Override
	public int createLivePoll(LivePoll livePoll, List<PollAnswer> answerList) {
		int key = insertLivePoll(livePoll);
		return insertPollAnswer(key, answerList);
	}

	@Transactional
	public boolean modifyLivePoll(LivePoll livePoll, List<PollAnswer> answerList) {
		int result = updateLivePoll(livePoll);
		if (result > 0) {
			updatePollAnswer(livePoll.getId(), answerList);
			return true;
		}

		return false;
	}

	public int updatePollAnswer(int livePollId, List<PollAnswer> answerList) {

		logger.info("update the poll answer.....");
		return livePollDaoImpl.updatePollAnswer(livePollId, answerList);

	}

	@Override
	public int updateActivePoll(Integer livePollId, String logonUserId) {
		logger.info("active the live poll");

		return livePollDaoImpl.updateActivePoll(livePollId, logonUserId);
	}

	@Override
	public int deleteLivePollById(Integer livePollId, String logonUserId) {
		logger.info("delete the live poll by id");

		int result = livePollDaoImpl.deleteLivePoll(livePollId, logonUserId);
		return result;
	}

	public Map<String, Object> queryUserGroupInfo(String logonUserId) {
		logger.info("get the user group info");
		Map<String, Object> result = livePollDaoImpl.queryUserGroupInfo(logonUserId);
		return result;
	}
}
