package com.none.web.service.exhibition;

import java.util.List;
import java.util.Map;

import com.none.core.model.Pager;
import com.none.web.model.LivePoll;
import com.none.web.model.PollAnswer;
import com.none.web.model.PollResult;
import com.none.web.po.UserAppPO;

public interface ILivePollAPIService {

	
	public List<Map<String, Object>> queryLivePoll(Pager pager, String platform, String logonUserId)  throws Exception;
	public List<Map<String, Object>> queryPollAnswer(int livePollId);
	public List<Map<String, Object>> queryPollResult(Pager pager, int livePollId);
	public Map<String, Object> queryLivePollByLivePollId(int livePollId, String platform, String logonUserId) throws Exception;
	public Map<String, Object> totalPoll(int livePollId);
	public Integer checkLogonUserIsAnswer(int livePollId, String logonUserId);
	
	public int insertLivePoll(LivePoll livePoll);
	public int updateLivePoll(LivePoll livePoll);
	public int insertPollAnswer(int key, List<PollAnswer> pollAnswerList);
	public int updatePollAnswer(PollAnswer pollAnswer, UserAppPO userAppPO);
	
	public int createLivePoll(LivePoll livePoll, List<PollAnswer> answerList);
	public boolean modifyLivePoll(LivePoll livePoll, List<PollAnswer> answerList);
	public int updateActivePoll(Integer livePollId, String logonUserId);
	public int deleteLivePollById(Integer integer, String logonUserId);
}
