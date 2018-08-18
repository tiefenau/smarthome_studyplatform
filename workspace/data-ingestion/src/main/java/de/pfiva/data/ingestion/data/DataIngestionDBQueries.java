package de.pfiva.data.ingestion.data;

public interface DataIngestionDBQueries {

	// Insert queries ------------------------------------------------------------------------------
	public static final String INSERT_QUERY_TBL = "INSERT INTO query_tbl(user_query,"
			+ " hotword, query_timestamp, file_location) VALUES(?,?,?,?)";
	
	public static final String INSERT_INTENT_TBL = "INSERT INTO intent_tbl(intent_name,"
			+ " query_id) VALUES(?,?)";
	
	public static final String INSERT_SLOTS_TBL = "INSERT INTO slots_tbl(slot_name,"
			+ " slot_value, intent_id) VALUES(?,?,?)";
	
	public static final String INSERT_FEEDBACK_QUERY = "INSERT INTO feedback_tbl(feedback_query,"
			+ " query_id) VALUES(?,?)";
	
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
			+ " f.feedback_id, f.feedback_query, f.user_response, f.feedback_timestamp"
			+ " from query_tbl as q LEFT OUTER JOIN intent_tbl as i"
			+ " ON q.query_id = i.query_id"
			+ " LEFT OUTER JOIN feedback_tbl as f"
			+ " ON q.query_id = f.query_id";

	public static final String GET_SLOTS_BY_INTENT_ID = "select slot_name, slot_value"
			+ " from slots_tbl where intent_id = ?";
	
	public static final String GET_CLIENT_TOKEN = "SELECT client_token from clients_tbl"
			+ " where client_name = ?";
}
