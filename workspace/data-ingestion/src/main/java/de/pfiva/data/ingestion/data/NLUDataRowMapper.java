package de.pfiva.data.ingestion.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import de.pfiva.data.ingestion.model.Feedback;
import de.pfiva.data.ingestion.model.NLUData;
import de.pfiva.data.ingestion.model.snips.Intent;
import de.pfiva.data.ingestion.model.snips.SnipsOutput;

public class NLUDataRowMapper implements RowMapper<NLUData> {

	@Override
	public NLUData mapRow(ResultSet rs, int rowNum) throws SQLException {
		NLUData nluData = new NLUData();
		
		SnipsOutput snipsOutput = new SnipsOutput();
		snipsOutput.setId(String.valueOf(rs.getInt("query_id")));
		snipsOutput.setInput(rs.getString("user_query"));
		snipsOutput.setHotword(rs.getString("hotword"));
		
		Timestamp queryTimestamp = rs.getTimestamp("query_timestamp");
		snipsOutput.setTimestamp(queryTimestamp != null ? queryTimestamp.toLocalDateTime() : null);
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
		
		Timestamp feedbackTimestamp = rs.getTimestamp("feedback_timestamp");
		feedback.setTimestamp(feedbackTimestamp != null ? feedbackTimestamp.toLocalDateTime() : null);
		nluData.setFeedback(feedback);
		
		return nluData;
	}
}
