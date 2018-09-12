package de.pfiva.data.ingestion.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.User;
import de.pfiva.data.model.snips.Intent;
import de.pfiva.data.model.snips.Slot;
import de.pfiva.data.model.snips.SnipsOutput;

@Service
public class NLUDataIngestionDBService {

	private static Logger logger = LoggerFactory.getLogger(NLUDataIngestionDBService.class);
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	public int ingestDataToDB(SnipsOutput snipsOutput) {
		if(snipsOutput == null) {
			logger.info("Nothing to push in database.");
			return 0;
		}
		
		// Insert in query_tbl
		int queryTableRowId = insertIntoQueryTable(snipsOutput);
		
		
		// Insert in intent_tbl
		if(snipsOutput.getIntent() != null) {
			int intentTableRowId = insertIntoIntentTable(snipsOutput.getIntent(), queryTableRowId);
			
			// Insert in slots_tbl
			if(snipsOutput.getSlots() != null) {
				insertIntoSlotsTable(snipsOutput.getSlots(), intentTableRowId);
			}
		}
		
		logger.info("Insertion in databse successful.");
		return queryTableRowId;
	}

	private int insertIntoQueryTable(SnipsOutput snipsOutput) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_QUERY_TBL, new String[] {"query_id"});
				
				ps.setString(1, snipsOutput.getInput());
				ps.setString(2, snipsOutput.getHotword());
				ps.setString(3, snipsOutput.getTimestamp());
				
				String completeFileLocation = null;
				if(snipsOutput.getFilename() != null && snipsOutput.getFilePath() != null) {
					completeFileLocation = snipsOutput.getFilePath()
							+ snipsOutput.getFilename();
					
				}
				ps.setString(4, completeFileLocation);
				
				return ps;
			}
		}, keyHolder);
		
		int rowId = keyHolder.getKey().intValue();
		logger.info("Inserted record in query_tbl with row id [" + rowId + "]");
		return rowId;
	}
	
	private int insertIntoIntentTable(Intent intent, int queryTableRowId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_INTENT_TBL, new String[] {"intent_id"});
				
				ps.setString(1, intent.getIntentName());
				ps.setInt(2, queryTableRowId);
				
				return ps;
			}
		}, keyHolder);
		
		int rowId = keyHolder.getKey().intValue();
		logger.info("Inserted record in intent_tbl with row id [" + rowId + "]");
		return rowId;
	}
	
	private void insertIntoSlotsTable(List<Slot> slots, int intentTableRowId) {
		jdbcTemplate.batchUpdate(DataIngestionDBQueries.INSERT_SLOTS_TBL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, slots.get(i).getSlotName());
				ps.setString(2, slots.get(i).getRawValue());
				ps.setInt(3, intentTableRowId);
			}
			
			@Override
			public int getBatchSize() {
				return slots.size();
			}
		});
		
		logger.info("Inserted [" + slots.size() + "] records in slots_tbl");
	}

	public void saveClientRegistrationTokenToDB(String clientName, String token) {
		if(token != null && !token.isEmpty()) {
			
			// If client does not exists then insert other wise update token
			
			if(clientName != null && !clientName.isEmpty()) {
				List<String> clientNames = jdbcTemplate
						.query(DataIngestionDBQueries.FETCH_CLIENT_NAMES, new RowMapper<String>() {

					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {						
						return rs.getString("client_name");
					}
				});
				
				logger.info("Existing clients : " + clientNames);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
				String currentDateTime = LocalDateTime.now().format(formatter);
				
				if(clientNames.contains(clientName)) {
					// Update client token
					logger.info("Client [" + clientName + "] exists, therefore updating client token.");
					
					jdbcTemplate.update(DataIngestionDBQueries.UPDATE_CLIENT_TOKEN, token, currentDateTime, clientName);
				} else {
					// Insert new client details
					logger.info("Client [" + clientName + "] does not exists,"
							+ " therefore inserting details for new client.");
					
					jdbcTemplate.update(DataIngestionDBQueries.INSERT_CLIENTS_TBL, clientName, token, currentDateTime);
				}
			}
		}
	}

	public List<NLUData> getQueryIntentFeedbackData() {
		logger.info("Fetching Query, Intent, Feedback data");
		return jdbcTemplate.query(DataIngestionDBQueries.GET_QUERY_INTENT_FEEDBACK_DATA, new NLUDataRowMapper());
	}

	public List<Slot> getSlotsForIntent(int intentId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_SLOTS_BY_INTENT_ID,
				new Object[] { intentId }, new RowMapper<Slot>() {

			@Override
			public Slot mapRow(ResultSet rs, int rowNum) throws SQLException {
				Slot slot = new Slot();
				slot.setSlotName(rs.getString("slot_name"));
				slot.setRawValue(rs.getString("slot_value"));
				
				return slot;
			}
		});
	}

	public String getClientToken(String clientName) {
		return jdbcTemplate.queryForObject(DataIngestionDBQueries.GET_CLIENT_TOKEN,
				String.class, clientName);
	}

	public int pushFeedbackToDB(int queryId, String feedbackQuery) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_FEEDBACK_QUERY,
								new String[] {"feedback_id"});
				
				ps.setString(1, feedbackQuery);
				ps.setInt(2, queryId);
				return ps;
			}
		}, keyHolder);
		
		logger.info("Insertion into feedback_tbl successful");
		return keyHolder.getKey().intValue();
	}

	public int saveFeedbackResponse(int id, String userResponse, String timestamp) {
		return jdbcTemplate.update(DataIngestionDBQueries.INSERT_FEEDBACK_RESPONSE,
				userResponse, timestamp, id);
	}

	public List<User> getUsers() {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_USERS, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setDeviceId(rs.getString("device_id"));
				return user;
			}
		});
	}
}
