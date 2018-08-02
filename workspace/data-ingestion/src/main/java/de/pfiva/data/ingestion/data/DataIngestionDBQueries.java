package de.pfiva.data.ingestion.data;

public interface DataIngestionDBQueries {

	public static final String INSERT_QUERY_TBL = "INSERT INTO query_tbl(user_query,"
			+ " hotword, query_timestamp, file_location) VALUES(?,?,?,?)";
	
	public static final String INSERT_INTENT_TBL = "INSERT INTO intent_tbl(intent_name,"
			+ " query_id) VALUES(?,?)";
	
	public static final String INSERT_SLOTS_TBL = "INSERT INTO slots_tbl(slot_name,"
			+ " slot_value, intent_id) VALUES(?,?,?)";
}
