package com.none.web.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import com.none.core.model.Pager;
import com.none.web.model.LivePoll;
import com.none.web.model.PollAnswer;
import com.none.web.model.PollResult;

public interface ILivePollDao {

	public Map<String, Object> queryTotalPoll(int livePollId);

	public List<Map<String, Object>> queryLivePoll(Pager pager, String platform);

	public List<Map<String, Object>> queryPollAnswer(int livePollId);

	public List<Map<String, Object>> queryPollResult(int livePollId, Pager pager);

	public Map<String, Object> queryLivePollById(int livePollId);

	public Map<String, Object> checkLogonUserIsAnswer(int livePollId, String logonUserId) throws DataAccessException;

	public int insertLivePoll(LivePoll livePoll);

	public int updateLivePoll(LivePoll livePoll);

	public int insertPollAnswer(int key, List<PollAnswer> pollAnswerList);

	public int updatePollAnswer(PollAnswer pollAnswer);

	public int deleteLivePoll(int livePollId, String logonUserId);

	public int insertPollResult(PollResult pollResult);

	public int updatePollAnswer(int livePollId, List<PollAnswer> pollAnswerList);

	public int updateActivePoll(Integer livePollId, String logonUserId);

	public Map<String, Object> queryUserGroupInfo(String logonUserId);
}
