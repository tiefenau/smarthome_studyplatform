package de.pfiva.data.ingestion.data;

public interface DataIngestionDBQueries {

	// Insert queries ------------------------------------------------------------------------------
	public static final String INSERT_QUERY_TBL = "INSERT INTO query_tbl(user_query,"
			+ " hotword, query_timestamp, file_location, user_id) VALUES(?,?,?,?,?)";
	
	public static final String INSERT_INTENT_TBL = "INSERT INTO intent_tbl(intent_name,"
			+ " query_id) VALUES(?,?)";
	
	public static final String INSERT_SLOTS_TBL = "INSERT INTO slots_tbl(slot_name,"
			+ " slot_value, intent_id) VALUES(?,?,?)";
	
	public static final String INSERT_FEEDBACK_QUERY = "INSERT INTO feedback_tbl(feedback_query,"
			+ " query_id) VALUES(?,?)";
	
	public static final String INSERT_FEEDBACK_RESPONSE = "UPDATE feedback_tbl SET user_response = ?,"
			+ " feedback_timestamp = ?"
			+ " WHERE feedback_id = ?";
	
	public static final String INSERT_MESSAGES_TBL = "INSERT INTO messages_tbl(message_text,"
			+ " status, delivery_date, topic_id) VALUES(?,?,?,?)";
	
	public static final String INSERT_MESSAGE_USERS_TBL = "INSERT INTO message_users_tbl(message_id, user_id) VALUES(?,?)";
	
	public static final String INSERT_USER_TBL = "INSERT INTO user_tbl(username, device_id) VALUES (?,?)";
	
	public static final String INSERT_SURVEY_TBL = "INSERT INTO survey_tbl(survey_name,"
			+ " status, delivery_date, topic_id) VALUES(?,?,?,?)";
	
	public static final String INSERT_SURVEY_USERS_TBL = "INSERT INTO survey_users_tbl(survey_id, user_id) VALUES(?,?)";
	
	public static final String INSERT_QUESTION_TBL = "INSERT INTO questions_tbl(question, question_type, survey_id) VALUES(?,?,?)";
	
	public static final String INSERT_OPTIONS_TBL = "INSERT INTO options_tbl(value, question_id) VALUES(?,?)";
	
	public static final String INSERT_MESSAGE_RESPONSE = "INSERT INTO message_response_tbl(value,"
			+ " message_id, user_id) VALUES(?,?,?)";
	
	public static final String INSERT_SURVEY_RESPONSE_TBL = "INSERT INTO survey_response_tbl(value, question_id,"
			+ " survey_id, user_id) VALUES(?,?,?,?)";
	
	public static final String INSERT_INTO_TOPIC_TBL = "INSERT INTO topic_tbl(topic_name, creation_date,"
			+ " modification_date) VALUES(?,?,?)";
	
	// Client table queries ------------------------------------------------------------------------
	public static final String FETCH_CLIENT_NAMES = "SELECT client_name from clients_tbl";
	
	public static final String INSERT_CLIENTS_TBL = "INSERT INTO clients_tbl(client_name,"
			+ " client_token, token_timestamp) VALUES(?,?,?)";
			
	public static final String UPDATE_CLIENT_TOKEN = "UPDATE clients_tbl SET client_token"
			+ " = ?, token_timestamp = ?"
			+ " WHERE client_name = ?";
	
	// Get queries ---------------------------------------------------------------------------------
	public static final String GET_QUERY_INTENT_FEEDBACK_DATA = "SELECT q.query_id, q.user_query,"
			+ " q.hotword, q.query_timestamp, q.file_location, i.intent_id, i.intent_name,"
			+ " f.feedback_id, f.feedback_query, f.user_response, f.feedback_timestamp,"
			+ " u.user_id, u.username, u.device_id"
			+ " from query_tbl as q LEFT OUTER JOIN intent_tbl as i"
			+ " ON q.query_id = i.query_id"
			+ " LEFT OUTER JOIN feedback_tbl as f"
			+ " ON q.query_id = f.query_id"
			+ " INNER JOIN user_tbl u ON q.user_id = u.user_id"
			+ " ORDER BY q.query_id";
	
	public static final String GET_QUERY_INTENT_FEEDBACK_DATA_BY_USER = "SELECT q.query_id, q.user_query,"
			+ " q.hotword, q.query_timestamp, q.file_location, i.intent_id, i.intent_name,"
			+ " f.feedback_id, f.feedback_query, f.user_response, f.feedback_timestamp,"
			+ " u.user_id, u.username, u.device_id"
			+ " from query_tbl as q LEFT OUTER JOIN intent_tbl as i"
			+ " ON q.query_id = i.query_id"
			+ " LEFT OUTER JOIN feedback_tbl as f"
			+ " ON q.query_id = f.query_id"
			+ " INNER JOIN user_tbl u"
			+ " ON q.user_id = u.user_id AND u.user_id = ?"
			+ " ORDER BY q.query_id";

	public static final String GET_SLOTS_BY_INTENT_ID = "select slot_name, slot_value"
			+ " from slots_tbl where intent_id = ?";
	
	public static final String GET_CLIENT_TOKEN = "SELECT client_token from clients_tbl"
			+ " where client_name = ?";
	
	public static final String GET_USERS = "SELECT * from user_tbl";
	
	public static final String GET_MESSAGES = "SELECT m.message_id, m.message_text,"
			+ " m.status, m.delivery_date, t.topic_name from messages_tbl as m"
			+ " INNER JOIN topic_tbl as t ON m.topic_id = t.topic_id ORDER BY m.message_id ASC";
	
	public static final String GET_MESSAGE = "SELECT m.message_id, m.message_text,"
			+ " m.status, m.delivery_date, t.topic_name from messages_tbl as m"
			+ " INNER JOIN topic_tbl as t ON m.topic_id = t.topic_id AND message_id = ?";
	
	public static final String GET_MESSAGES_BY_TOPIC = "select m.message_id, m.message_text, m.status, m.delivery_date,"
			+ " t.topic_name from messages_tbl as m INNER JOIN topic_tbl as t"
			+ " ON m.topic_id = t.topic_id AND m.topic_id ="
			+ " (SELECT topic_id from topic_tbl where topic_name = ?) ORDER BY m.message_id ASC";
	
	public static final String GET_MESSAGE_RESPONSES = "select r.response_id, r.value, u.user_id,"
			+ " u.username from message_response_tbl as r JOIN user_tbl as u"
			+ " ON r.user_id = u.user_id where r.message_id = ?";
	
	public static final String GET_USERS_BY_MESSAGE_ID = "SELECT u.user_id, u.username, u.device_id"
			+ " from user_tbl as u JOIN message_users_tbl as m"
			+ " ON u.user_id = m.user_id where m.message_id = ?";
	
	public static final String GET_CONFIG_DATA = "SELECT * from configuration_tbl";
	
	public static final String GET_CONFIG_DATA_BY_KEY = "SELECT * from configuration_tbl where config_key = ?";
	
	public static final String GET_USERS_BY_SURVEY_ID = "SELECT u.user_id, u.username, u.device_id"
			+ " from user_tbl as u JOIN survey_users_tbl as s"
			+ " ON u.user_id = s.user_id where s.survey_id = ?";
	
	//public static final String GET_SURVEYS = "SELECT * from survey_tbl";
	
	//public static final String GET_SURVEY = "SELECT * from survey_tbl where survey_id = ?";
	
	public static final String GET_SURVEYS = "SELECT s.survey_id, s.survey_name,"
			+ " s.status, s.delivery_date, t.topic_name from survey_tbl as s"
			+ " INNER JOIN topic_tbl as t ON s.topic_id = t.topic_id ORDER BY s.survey_id ASC";
	
	public static final String GET_SURVEY = "SELECT s.survey_id, s.survey_name,"
			+ " s.status, s.delivery_date, t.topic_name from survey_tbl as s"
			+ " INNER JOIN topic_tbl as t ON s.topic_id = t.topic_id AND survey_id = ?";
	
	public static final String GET_SURVEYS_BY_TOPIC = "select s.survey_id, s.survey_name, s.status, s.delivery_date,"
			+ " t.topic_name from survey_tbl as s INNER JOIN topic_tbl as t"
			+ " ON s.topic_id = t.topic_id AND s.topic_id ="
			+ " (SELECT topic_id from topic_tbl where topic_name = ?) ORDER BY s.survey_id ASC";
	
	public static final String GET_QUESTIONS_BY_SURVEY_ID = "SELECT question_id,"
			+ " question, question_type from questions_tbl where survey_id = ?";
	
	public static final String GET_OPTIONS_PER_QUESTION = "SELECT option_id,"
			+ " value from options_tbl where question_id = ?";
	
	public static final String GET_SURVEY_RESPONSES_BY_ID = "select s.response_id, s.question_id,"
			+ " u.user_id, u.username, u.device_id from survey_response_tbl as s"
			+ " INNER JOIN user_tbl as u"
			+ " ON s.user_id = u.user_id AND s.survey_id = ?";
	
	public static final String GET_SURVEY_QUES_VALUES = "select value from survey_response_tbl where"
			+ " question_id = ? and user_id = ?";
	
	public static final String GET_DEVICE_ID = "SELECT device_id from user_tbl where username = ?";
	
	public static final String GET_TOPIC_ID = "SELECT topic_id from topic_tbl where topic_name = ?";
	
	public static final String GET_TOPICS = "SELECT * from topic_tbl";
	
	public static final String GET_SURVEY_COUNT_BY_TOPIC_ID = "select count(survey_id)"
			+ " from survey_tbl where topic_id = ?";
	
	public static final String GET_MESSAGE_COUNT_BY_TOPIC_ID = "select count(message_id)"
			+ " from messages_tbl where topic_id = ?";
	
	public static final String GET_TOPIC_NAMES = "SELECT topic_name from topic_tbl";
	
	public static final String GET_USERID_BY_USERNAME = "SELECT user_id from user_tbl where username = ?";
	
	public static final String GET_USERID_BY_DEVICEID = "SELECT user_id from user_tbl where device_id = ?";
	
	// Update queries ------------------------------------------------------------------------------
	public static final String UPDATE_MESSAGE_STATUS = "UPDATE messages_tbl SET status = ? where message_id = ?";
	
	public static final String UPDATE_CONFIG_VALUE = "UPDATE configuration_tbl SET"
			+ " config_value = ? where config_key = ?";
	
	public static final String UPDATE_SURVEY_STATUS = "UPDATE survey_tbl SET status = ? where survey_id = ?";
	
	public static final String UPDATE_MODIFIACTION_DATE_FOR_TOPIC = "UPDATE topic_tbl"
			+ " SET modification_date = ? where topic_id = ?";

	// Delete queries ------------------------------------------------------------------------------
	public static final String DELETE_USER = "DELETE from user_tbl where user_id = ?";
	
	public static final String DELETE_SURVEYS = "DELETE from survey_tbl where topic_id = ?";
	
	public static final String DELETE_MESSAGES = "DELETE from messages_tbl where topic_id = ?";
	
	public static final String DELETE_TOPIC = "DELETE from topic_tbl where topic_id = ?";
}
