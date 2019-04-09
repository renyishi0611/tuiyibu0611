package com.none.web.dao.exhibition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.none.core.model.Pager;
import com.none.web.dao.ILivePollDao;
import com.none.web.dao.PageUtilTool;
import com.none.web.model.LivePoll;
import com.none.web.model.PollAnswer;
import com.none.web.model.PollResult;

@Repository
public class LivePollDaoImpl implements ILivePollDao {

	public static Logger logger = Logger.getLogger(LivePollDaoImpl.class);

	@Autowired
	private PageUtilTool pageTool;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> queryTotalPoll(int livePollId) {
		String totalSql = "select sum(poll_score) total from t_live_poll_answer where poll_id=" + livePollId;
		return jdbcTemplate.queryForMap(totalSql);
	}

	@Override
	public List<Map<String, Object>> queryLivePoll(Pager pager, String platform) {

		String where = " and is_hide=1";
		String columns = "";
		if (StringUtils.equals("cms", platform)) {
			where = "";
			columns = " is_hide as activeOrHide,";
		}
		String querySql = "select id as livePollId, content as content, img_path as imgPath, submit_user as submitUserId, submit_time as submitTime, `status` as `status`,"
				+ columns
				+ " lastupdate_user as lastupdateUserId, lastupdate_time as lastUpdateTime, case when start_time < now() and now() < end_time then 0 else 1 end isExpired from t_live_poll where 1=1"
				+ where + " and is_delete=0 order by submit_time DESC";
		return pageTool.QueryPerPage(querySql, pager);
	}

	@Override
	public List<Map<String, Object>> queryPollAnswer(int livePollId) {
		String querySql = "select id as answerId, answer as answerContent, poll_score as score from t_live_poll_answer where is_delete=0 and poll_id="
				+ livePollId;
		return jdbcTemplate.queryForList(querySql);
	}

	@Override
	public List<Map<String, Object>> queryPollResult(int livePollId, Pager pager) {
		String querySql = "select id, user_id, user_name, poll_answer_id, live_poll_id, answer_date from t_live_poll_result where 1=1 and live_poll_id="
				+ livePollId;
		return pageTool.QueryPerPage(querySql, pager);
	}

	@Override
	public Map<String, Object> queryLivePollById(int livePollId) {
		String querySql = "select id as livePollId, content as content, img_path as imgPath, submit_user as submitUser, "
				+ "submit_time as submitTime, lastupdate_time as lastUpdateTime, "
				+ "`status` as `status`, is_hide as activeOrHide, start_time as startTime, end_time as endTime, submit_type as voteNow from t_live_poll where 1=1 and id="
				+ livePollId;
		List<Map<String, Object>> pollLists = jdbcTemplate.queryForList(querySql);

		if (null != pollLists && pollLists.size() > 0) {
			return pollLists.get(0);
		}

		return null;
	}

	@Override
	public Map<String, Object> checkLogonUserIsAnswer(int livePollId, String logonUserId) throws DataAccessException {
		String querySql = "select poll_answer_id as isAnswer  from t_live_poll_result where live_poll_id= ? and user_id= ? limit 1";
		Object[] param = { livePollId, logonUserId };
		Map<String, Object> result = null;
		try {
			result = jdbcTemplate.queryForMap(querySql, param);
		} catch (DataAccessException e) {
			logger.info("any person has polled answer: " + e.getMessage());
		}
		return result;
	}

	@Override
	public int insertLivePoll(final LivePoll livePoll) {

		final String sql = "insert into t_live_poll (content, img_path, submit_user, lastupdate_user, `status`, is_hide, start_time, end_time, submit_type) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		final Timestamp startTime = StringUtils.isBlank(livePoll.getStartTime())
				&& StringUtils.equals("now", livePoll.getVoteNow()) ? new Timestamp(System.currentTimeMillis())
						: new Timestamp(NumberUtils.toLong(livePoll.getStartTime()));

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql,
						new String[] { "content", "img_path", "submit_user", "lastupdate_user", "`status`", "is_hide",
								"start_time", "end_time" }); // , "submit_type"
				ps.setString(1, livePoll.getContent());
				ps.setString(2, livePoll.getImgPath());
				ps.setString(3, livePoll.getSubmitUser());
				ps.setString(4, livePoll.getLastupdateUser());
				ps.setInt(5, livePoll.getStatus().getIndex());
				ps.setBoolean(6, StringUtils.equals("active", livePoll.getActiveOrHide()));
				ps.setTimestamp(7, startTime);
				ps.setTimestamp(8, new Timestamp(NumberUtils.toLong(livePoll.getEndTime())));
				ps.setString(9, livePoll.getVoteNow());
				return ps;
			}

		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	@Override
	public int updateLivePoll(LivePoll livePoll) {
		String updateSql = "update t_live_poll set content=?, img_path=?, lastupdate_user=now(), `status`=?, is_hide=?, start_time=?, end_time=?, submit_type=?"
				+ " where id=?";
		Timestamp startTime = StringUtils.isBlank(livePoll.getStartTime())
				&& StringUtils.equals("now", livePoll.getVoteNow()) ? new Timestamp(System.currentTimeMillis())
						: new Timestamp(NumberUtils.toLong(livePoll.getStartTime()));

		Object[] param = { livePoll.getContent(), livePoll.getImgPath(), livePoll.getStatus().getIndex(),
				StringUtils.equals("active", livePoll.getActiveOrHide()), startTime,
				new Timestamp(NumberUtils.toLong(livePoll.getEndTime())), livePoll.getVoteNow(), livePoll.getId() };
		int result = jdbcTemplate.update(updateSql, param);
		return result;
	}

	@Override
	public int insertPollAnswer(int key, List<PollAnswer> pollAnswerList) {
		String insertSql = "insert into t_live_poll_answer (poll_id, answer) values (" + key + ", ?)";

		List<Object[]> listArray = new ArrayList<Object[]>();
		Object[] params = null;

		for (PollAnswer answer : pollAnswerList) {
			params = new Object[] { answer.getAnswer() };
			listArray.add(params);
		}

		int[] result = jdbcTemplate.batchUpdate(insertSql, listArray);
		return result.length;
	}

	@Override
	public int updatePollAnswer(PollAnswer pollAnswer) {
		String updateSql = "update t_live_poll_answer set poll_score=poll_score+1 where id=? and poll_id=?";
		Object[] param = { pollAnswer.getId(), pollAnswer.getPollId() };
		int result = 0;
		result = jdbcTemplate.update(updateSql, param);

		return result;
	}

	@Override
	public int insertPollResult(PollResult pollResult) {
		String insertSql = "insert into t_live_poll_result(user_id, user_name, poll_answer_id, live_poll_id) values(?, ?, ?, ?)";
		Object[] param = { pollResult.getUserId(), pollResult.getUserName(), pollResult.getPollAnswerId(),
				pollResult.getLive_pollId() };
		int result = jdbcTemplate.update(insertSql, param);

		return result;
	}

	@Override
	public int deleteLivePoll(int livePollId, String logonUserId) {
		String deleteLivePollSql = "update t_live_poll set is_delete=1, lastupdate_user='" + logonUserId
				+ "', lastupdate_time=now() where 1=1 and (end_time < now() or `status`=0) and id=" + livePollId;
		int result = jdbcTemplate.update(deleteLivePollSql);
		
		String deletePollAnswerSql = "update t_live_poll_answer set is_delete=1 where poll_id=" + livePollId;
		if(result > 0)
		{
			result = jdbcTemplate.update(deletePollAnswerSql);
		}

		return result;

	}

	@Override
	public int updatePollAnswer(int livePollId, List<PollAnswer> pollAnswerList) {
		String deleteSql = "delete from t_live_poll_answer where poll_id=" + livePollId;

		int result = 0;
		result = jdbcTemplate.update(deleteSql);
		if (result > 0) {
			result = insertPollAnswer(livePollId, pollAnswerList);
		}

		return result;
	}

	@Override
	public int updateActivePoll(Integer livePollId, String logonUserId) {
		String activeSql = "update t_live_poll set is_hide = case is_hide when 1  then 0 else 1 end, lastupdate_user=?, lastupdate_time=now() WHERE id=?";
		Object[] param = { logonUserId, livePollId };
		return jdbcTemplate.update(activeSql, param);
	}

	@Override
	public Map<String, Object> queryUserGroupInfo(String logonUserId) {
		String groupSql = "select id as user_name, bu as displayName, buPhoto as headPortrait from t_bu where isDelete=0 and id in (select groupId from t_user where user_id=?)";
		Object[] param = { logonUserId };
		Map<String, Object> result = null; 
		
		try
		{
			result = jdbcTemplate.queryForMap(groupSql, param);
		}catch(DataAccessException e)
		{
			logger.info("no get the query data or have an issue, please check " + logonUserId);
		}
		return result;
	}
}
