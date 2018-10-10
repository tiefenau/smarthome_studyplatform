package de.pfiva.data.ingestion.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.PfivaConfigData;
import de.pfiva.data.model.Topic;
import de.pfiva.data.model.Tuple;
import de.pfiva.data.model.User;
import de.pfiva.data.model.message.Message;
import de.pfiva.data.model.message.Message.MessageStatus;
import de.pfiva.data.model.message.Response;
import de.pfiva.data.model.snips.Intent;
import de.pfiva.data.model.snips.Slot;
import de.pfiva.data.model.snips.SnipsOutput;
import de.pfiva.data.model.survey.Option;
import de.pfiva.data.model.survey.Question;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.data.model.survey.Survey.SurveyStatus;

@Service
public class NLUDataIngestionDBService {

	private static Logger logger = LoggerFactory.getLogger(NLUDataIngestionDBService.class);
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	public int ingestDataToDB(SnipsOutput snipsOutput, int userId) {
		if(snipsOutput == null) {
			logger.info("Nothing to push in database.");
			return 0;
		}
		
		// Insert in query_tbl
		int queryTableRowId = insertIntoQueryTable(snipsOutput, userId);
		
		
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

	private int insertIntoQueryTable(SnipsOutput snipsOutput, int userId) {
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
				ps.setInt(5, userId);
				
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

	public List<NLUData> getQueryIntentFeedbackData(int userId) {
		if(userId == 0) {
			logger.info("Fetching all queries, intent, feedback data");
			return jdbcTemplate.query(DataIngestionDBQueries.GET_QUERY_INTENT_FEEDBACK_DATA, new NLUDataRowMapper());
		} else {
			logger.info("Fetching all queries, intent, feedback data for user id [" + userId + "]");
			return jdbcTemplate.query(DataIngestionDBQueries.GET_QUERY_INTENT_FEEDBACK_DATA_BY_USER,
					new Object[] {userId}, new NLUDataRowMapper());
		}
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

	public Tuple<Integer, Boolean> saveMessageToDB(Message message, int topicId) {
		// 1. Insert into messages_tbl
		// 2. Insert into message_users_tbl
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_MESSAGES_TBL,
								new String[] {"message_id"});

				ps.setString(1, message.getMessageText());
				ps.setString(2, message.getMessageStatus().toString());
				ps.setString(3, message.getDeliveryDateTime());
				ps.setInt(4, topicId);
				return ps;
			}
		}, keyHolder);
		
		final int messageId = keyHolder.getKey().intValue();
		logger.info("Inserted record in messages_tbl with row id [" + messageId + "]");
		
		int[] rows = jdbcTemplate.batchUpdate(DataIngestionDBQueries.INSERT_MESSAGE_USERS_TBL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				User user = message.getUsers().get(i);
				ps.setInt(1, messageId);
				ps.setInt(2, user.getId());
			}
			
			@Override
			public int getBatchSize() {
				return message.getUsers().size();
			}
		});
		
		if(rows.length > 0) {
			logger.info("Inserted [" + rows.length + "]records in message_users_tbl.");
			return new Tuple<>(messageId, true);
		}
		return new Tuple<>(messageId, false);
	}

	public void updateMessageStatus(int messageId, MessageStatus status) {
		jdbcTemplate.update(DataIngestionDBQueries.UPDATE_MESSAGE_STATUS, status.toString(), messageId);
		logger.info("Message status for message id [" + messageId + "] updated to [" + status + "]");
	}

	public List<Message> getMessages() {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_MESSAGES, new RowMapper<Message>() {

			@Override
			public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
				Message message = new Message();
				message.setId(rs.getInt("message_id"));
				message.setMessageText(rs.getString("message_text"));
				message.setMessageStatus(MessageStatus.valueOf(rs.getString("status")));
				message.setDeliveryDateTime(rs.getString("delivery_date"));
				message.setTopic(rs.getString("topic_name"));
				return message;
			}
			
		});
	}
	
	public Message getMessage(int messageId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_MESSAGE,
				new Object[] {messageId}, new ResultSetExtractor<Message>() {

			@Override
			public Message extractData(ResultSet rs) throws SQLException, DataAccessException {
				Message message = new Message();
				message.setId(rs.getInt("message_id"));
				message.setMessageText(rs.getString("message_text"));
				message.setMessageStatus(MessageStatus.valueOf(rs.getString("status")));
				message.setDeliveryDateTime(rs.getString("delivery_date"));
				message.setTopic(rs.getString("topic_name"));
				return message;
			}
		});
	}
	
	public List<Message> getMessagesByTopic(String topic) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_MESSAGES_BY_TOPIC,
				new Object[] {topic}, new RowMapper<Message>() {

			@Override
			public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
				Message message = new Message();
				message.setId(rs.getInt("message_id"));
				message.setMessageText(rs.getString("message_text"));
				message.setMessageStatus(MessageStatus.valueOf(rs.getString("status")));
				message.setDeliveryDateTime(rs.getString("delivery_date"));
				message.setTopic(rs.getString("topic_name"));
				return message;
			}
		});
	}
	
	public List<Response> getMessageResponses(int messageId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_MESSAGE_RESPONSES,
				new Object[] {messageId}, new RowMapper<Response>() {

			@Override
			public Response mapRow(ResultSet rs, int rowNum) throws SQLException {
				Response response = new Response();
				response.setId(rs.getInt("response_id"));
				response.setValue(rs.getString("value"));
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				response.setUser(user);
				return response;
			}
		});
	}

	public List<User> getUsersByMessageId(int messageId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_USERS_BY_MESSAGE_ID, 
				new Object[] {messageId}, new RowMapper<User>() {

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

	public boolean addNewUser(User user) {
		int status = jdbcTemplate.update(DataIngestionDBQueries.INSERT_USER_TBL,
				user.getUsername(), user.getDeviceId());
		if(status > 0) {
			logger.info("New user [" + user.getUsername() + "] added");
			return true;
		}
		return false;
	}

	public boolean deleteUser(int userId) {
		int status = jdbcTemplate.update(DataIngestionDBQueries.DELETE_USER, userId);
		if(status > 0) {
			logger.info("User with id [" + userId + "] deleted");
			return true;
		}
		return false;
	}

	public List<PfivaConfigData> getConfigurationValues() {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_CONFIG_DATA, new RowMapper<PfivaConfigData>() {

			@Override
			public PfivaConfigData mapRow(ResultSet rs, int rowNum) throws SQLException {
				PfivaConfigData data = new PfivaConfigData();
				data.setId(rs.getInt("config_id"));
				data.setKey(rs.getString("config_key"));
				data.setValue(rs.getString("config_value"));
				return data;
			}
		});
	}

	public void saveConfigValue(PfivaConfigData configData) {
		jdbcTemplate.update(DataIngestionDBQueries.UPDATE_CONFIG_VALUE, 
				configData.getValue(), configData.getKey());
		logger.info("Updated config key [" + configData.getKey()
			+ "], with value [" + configData.getValue() + "]");
	}

	public Tuple<Integer, Boolean> saveSurveyToDB(Survey survey, int topicId) {
		// 1. Insert into survey_tbl
		// 2. Insert into survey_users_tbl
		// 3. Insert into question_tbl
		// 4. Insert into option_tbl
		
		Tuple<Integer, Boolean> status = insertIntoSurveyTable(survey, topicId);
		insertIntoQuestionOptionTable(survey, status);
		return status;
	}

	private void insertIntoQuestionOptionTable(Survey survey, Tuple<Integer, Boolean> status) {
		// If insertion in survey table is successful
		if(status.getY()) {
			for(Question question : survey.getQuestions()) {
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con
								.prepareStatement(DataIngestionDBQueries.INSERT_QUESTION_TBL,
										new String[] {"question_id"});
						
						ps.setString(1, question.getQuestion());
						ps.setString(2, question.getQuestionType());
						ps.setInt(3, status.getX()); // status.getX() is surveyId
						return ps;
					}
				}, keyHolder);
				
				final int questionId = keyHolder.getKey().intValue();
				logger.info("Inserted record in question_tbl with row id [" + questionId + "]");
				
				if(!question.getQuestionType().equals("Text")) {
					int[] rows = jdbcTemplate.batchUpdate(DataIngestionDBQueries.INSERT_OPTIONS_TBL,
							new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Option option = question.getOptions().get(i);
							ps.setString(1, option.getValue());
							ps.setInt(2, questionId);
						}

						@Override
						public int getBatchSize() {
							return question.getOptions().size();
						}
					});
					
					if(rows.length > 0) {
						logger.info("Inserted [" + rows.length + "] records in"
								+ " options_tbl for question id [" + questionId + "].");
					}
				}
			}			
		}
	}

	private Tuple<Integer, Boolean> insertIntoSurveyTable(Survey survey, int topicId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_SURVEY_TBL,
								new String[] {"survey_id"});

				ps.setString(1, survey.getSurveyName());
				ps.setString(2, survey.getSurveyStatus().toString());
				ps.setString(3, survey.getDeliveryDateTime());
				ps.setInt(4, topicId);
				return ps;
			}
		}, keyHolder);

		final int surveyId = keyHolder.getKey().intValue();
		logger.info("Inserted record in survey_tbl with row id [" + surveyId + "]");

		int[] rows = jdbcTemplate.batchUpdate(DataIngestionDBQueries.INSERT_SURVEY_USERS_TBL,
				new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				User user = survey.getUsers().get(i);
				ps.setInt(1, surveyId);
				ps.setInt(2, user.getId());
			}

			@Override
			public int getBatchSize() {
				return survey.getUsers().size();
			}
		});

		if(rows.length > 0) {
			logger.info("Inserted [" + rows.length + "] records in survey_users_tbl.");
			return new Tuple<>(surveyId, true);
		}
		return new Tuple<>(surveyId, false);
	}
	
	public void updateSurveyStatus(int surveyId, SurveyStatus status) {
		jdbcTemplate.update(DataIngestionDBQueries.UPDATE_SURVEY_STATUS, status.toString(), surveyId);
		logger.info("Survey status for survey id [" + surveyId + "] updated to [" + status + "]");
	}
	
	public List<Survey> getSurveys() {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_SURVEYS, new RowMapper<Survey>() {

			@Override
			public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {
				Survey survey = new Survey();
				survey.setId(rs.getInt("survey_id"));
				survey.setSurveyName(rs.getString("survey_name"));
				survey.setSurveyStatus(SurveyStatus.valueOf(rs.getString("status")));
				survey.setDeliveryDateTime(rs.getString("delivery_date"));
				survey.setTopic(rs.getString("topic_name"));
				return survey;
			}
		});
	}
	
	public List<Survey> getSurveysByTopic(String topic) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_SURVEYS_BY_TOPIC,
				new Object[] {topic}, new RowMapper<Survey>() {

			@Override
			public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {
				Survey survey = new Survey();
				survey.setId(rs.getInt("survey_id"));
				survey.setSurveyName(rs.getString("survey_name"));
				survey.setSurveyStatus(SurveyStatus.valueOf(rs.getString("status")));
				survey.setDeliveryDateTime(rs.getString("delivery_date"));
				survey.setTopic(rs.getString("topic_name"));
				return survey;
			}
		});
	}
	
	public List<User> getUsersBySurveyId(int surveyId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_USERS_BY_SURVEY_ID, 
				new Object[] {surveyId}, new RowMapper<User>() {

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

	public List<Question> getQuestionsBySurveyId(int surveyId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_QUESTIONS_BY_SURVEY_ID, 
				new Object[] {surveyId}, new RowMapper<Question>() {

			@Override
			public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
				Question question = new Question();
				question.setId(rs.getInt("question_id"));
				question.setQuestion(rs.getString("question"));
				question.setQuestionType(rs.getString("question_type"));
				return question;
			}
		});
	}

	public List<Option> getOptionsForQuestion(int questionId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_OPTIONS_PER_QUESTION, 
				new Object[] {questionId}, new RowMapper<Option>() {

			@Override
			public Option mapRow(ResultSet rs, int rowNum) throws SQLException {
				Option option = new Option();
				option.setId(rs.getInt("option_id"));
				option.setValue(rs.getString("value"));
				return option;
			}
		});
	}

	public Survey getSurveyById(int surveyId) {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_SURVEY,
				new Object[] {surveyId}, new ResultSetExtractor<Survey>() {

			@Override
			public Survey extractData(ResultSet rs) throws SQLException, DataAccessException {
				Survey survey = new Survey();
				survey.setId(rs.getInt("survey_id"));
				survey.setSurveyName(rs.getString("survey_name"));
				survey.setSurveyStatus(SurveyStatus.valueOf(rs.getString("status")));
				survey.setDeliveryDateTime(rs.getString("delivery_date"));
				survey.setTopic(rs.getString("topic_name"));
				return survey;
			}
		});
	}

	public int saveMessageResponse(int messageId, Response response) {
		return jdbcTemplate.update(DataIngestionDBQueries.INSERT_MESSAGE_RESPONSE, 
				response.getValue(), messageId, response.getUser().getId());
	}

	public boolean saveSurveyResponse(int surveyId,
			List<de.pfiva.data.model.survey.Response> responses) {
		
		for(de.pfiva.data.model.survey.Response response : responses) {
			List<String> values = response.getValues();
			jdbcTemplate.batchUpdate(DataIngestionDBQueries.INSERT_SURVEY_RESPONSE_TBL,
					new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, values.get(i));
					ps.setInt(2, response.getQuestionId());
					ps.setInt(3, surveyId);
					ps.setInt(4, response.getUser().getId());
				}

				@Override
				public int getBatchSize() {
					return values.size();
				}
			});
		}
		
		logger.info("Saved user response for survey id [" + surveyId + "]");
		return true;
	}

	public List<de.pfiva.data.model.survey.Response> getSurveyResponse(int surveyId) {
		// TODO - This implementation is quite bad, can be improved.
		// Data model should be refactored.
		Map<Integer, de.pfiva.data.model.survey.Response> responseMap = 
				new HashMap<>();
		
		List<de.pfiva.data.model.survey.Response> responses = 
				jdbcTemplate.query(DataIngestionDBQueries.GET_SURVEY_RESPONSES_BY_ID,
						new Object[] {surveyId}, new RowMapper<de.pfiva.data.model.survey.Response>() {

					@Override
					public de.pfiva.data.model.survey.Response mapRow(ResultSet rs, int rowNum) throws SQLException {
						de.pfiva.data.model.survey.Response response = new de.pfiva.data.model.survey.Response();
						response.setId(rs.getInt("response_id"));
						response.setQuestionId(rs.getInt("question_id"));
						User user = new User();
						user.setId(rs.getInt("user_id"));
						user.setUsername(rs.getString("username"));
						user.setDeviceId(rs.getString("device_id"));
						response.setUser(user);
						return response;
					}
				});
		
		for(de.pfiva.data.model.survey.Response response : responses) {
			List<String> values = jdbcTemplate.query(DataIngestionDBQueries.GET_SURVEY_QUES_VALUES,
					new Object[] {response.getQuestionId()}, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("value");
				}
			});

			response.setValues(values);
			
			responseMap.put(response.getQuestionId(), response);
		}
		
		return new LinkedList<>(responseMap.values());
	}

	public String getDeviceId(String username) {
		return jdbcTemplate.queryForObject(DataIngestionDBQueries.GET_DEVICE_ID, String.class, username);
	}

	public int getTopicId(String topic) {
		int topicId = 0;
		try {
			topicId = jdbcTemplate
					.queryForObject(DataIngestionDBQueries.GET_TOPIC_ID, Integer.class, topic);	
		} catch(EmptyResultDataAccessException e) {
			logger.error("Topic does not exists");
			return topicId;
		}
		return topicId;
	}

	public void updateTopicModificationDate(int topicId, String date) {
		jdbcTemplate.update(DataIngestionDBQueries.UPDATE_MODIFIACTION_DATE_FOR_TOPIC, date, topicId);
		
	}

	public int createNewTopic(String topic, String date) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_INTO_TOPIC_TBL,
								new String[] {"topic_id"});

				ps.setString(1, topic);
				ps.setString(2, date);
				ps.setString(3, date);
				
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	public List<Topic> getTopics() {
		return jdbcTemplate.query(DataIngestionDBQueries.GET_TOPICS, new RowMapper<Topic>() {

			@Override
			public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
				Topic topic = new Topic();
				topic.setId(rs.getInt("topic_id"));
				topic.setTopicname(rs.getString("topic_name"));
				topic.setCreationDate(rs.getString("creation_date"));
				topic.setModificationDate(rs.getString("modification_date"));
				return topic;
			}
		});
	}

	public int getSurveyCount(int topicId) {
		return jdbcTemplate.queryForObject(DataIngestionDBQueries.GET_SURVEY_COUNT_BY_TOPIC_ID,
				Integer.class, topicId);
	}
	
	public int getMessageCount(int topicId) {
		return jdbcTemplate.queryForObject(DataIngestionDBQueries.GET_MESSAGE_COUNT_BY_TOPIC_ID,
				Integer.class, topicId);
	}
	
	public List<String> getTopicNames() {
		return jdbcTemplate.queryForList(DataIngestionDBQueries.GET_TOPIC_NAMES, String.class);
	}

	public boolean deleteSurveys(int topicId) {
		// TODO delete from survey_users_tbl, survey_response_tbl, questions_tbl, options_tbl
		int rows = jdbcTemplate.update(DataIngestionDBQueries.DELETE_SURVEYS, topicId);
		if(rows > 0) {
			logger.info("Deleted surveys with topic id [" + topicId + "]");
			return true;
		}
		return false;
	}

	public boolean deleteMessages(int topicId) {
		// TODO delete from message_response_tbl, message_users_tbl
		int rows = jdbcTemplate.update(DataIngestionDBQueries.DELETE_MESSAGES, topicId);
		if(rows > 0) {
			logger.info("Deleted messages with topic id [" + topicId + "]");
			return true;
		}
		return false;
	}

	public boolean deleteTopic(int topicId) {
		int rows = jdbcTemplate.update(DataIngestionDBQueries.DELETE_TOPIC, topicId);
		if(rows > 0) {
			logger.info("Deleted topic with id [" + topicId + "]");
			return true;
		}
		return false;
	}

	public int getUserId(String username) {
		return jdbcTemplate.queryForObject(DataIngestionDBQueries.GET_USERID_BY_USERNAME,
				Integer.class, username);
	}
	
	public int getUserByDeviceId(String deviceId) {
		return jdbcTemplate.queryForObject(DataIngestionDBQueries.GET_USERID_BY_DEVICEID,
				Integer.class, deviceId);
	}
}
