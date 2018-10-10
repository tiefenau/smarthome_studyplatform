package de.pfiva.data.ingestion.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.core.RowMapper;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.User;
import de.pfiva.data.model.snips.Intent;
import de.pfiva.data.model.snips.SnipsOutput;

public class NLUDataRowMapper implements RowMapper<NLUData> {

	@Override
	public NLUData mapRow(ResultSet rs, int rowNum) throws SQLException {
		NLUData nluData = new NLUData();
		
		SnipsOutput snipsOutput = new SnipsOutput();
		snipsOutput.setId(String.valueOf(rs.getInt("query_id")));
		snipsOutput.setInput(rs.getString("user_query"));
		snipsOutput.setHotword(rs.getString("hotword"));
		snipsOutput.setTimestamp(getTimestampInString(rs.getTimestamp("query_timestamp")));
		snipsOutput.setFilePath(rs.getString("file_location"));
		
		Intent intent = new Intent();
		intent.setIntentId(rs.getInt("intent_id"));
		intent.setIntentName(rs.getString("intent_name"));
		snipsOutput.setIntent(intent);
		
		nluData.setSnipsOutput(snipsOutput);
		
		Feedback feedback = new Feedback();
		feedback.setId(rs.getInt("feedback_id"));
		feedback.setFeedbackQuery(rs.getString("feedback_query"));
		feedback.setUserResponse(rs.getString("user_response"));
		feedback.setTimestamp(getTimestampInString(rs.getTimestamp("feedback_timestamp")));
		
		nluData.setFeedback(feedback);
		
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setUsername(rs.getString("username"));
		user.setDeviceId(rs.getString("device_id"));
		
		nluData.setUser(user);
		
		return nluData;
	}
	
	private String getTimestampInString(Timestamp timestamp) {
		if(timestamp != null) {
			return new SimpleDateFormat(Constants.DATE_TIME_FORMAT).format(timestamp);
		}
		return null;
	}
}
