package de.pfiva.data.ingestion;

public interface Constants {

	//2018-07-11T18_18_23.608512558+00_00-hotword-hey_snips
	
	public static final String HOTWORD_JSON_REGX = "\\d+\\-\\d+\\-\\d+T"
			+ "\\d+\\:\\d+\\:\\d+\\."
			+ "\\d+\\+\\d+\\:\\d+\\-hotword\\-\\w+\\_\\w+\\.json";
	
	public static final String HOTWORD_WAV_REGX = "\\d+\\-\\d+\\-\\d+T"
			+ "\\d+\\:\\d+\\:\\d+\\."
			+ "\\d+\\+\\d+\\:\\d+\\-hotword\\-\\w+\\_\\w+\\.wav";
	
	public static final String QUERY_JSON_REGX = "\\d+\\-\\d+\\-\\d+T"
			+ "\\d+\\:\\d+\\:\\d+\\."
			+ "\\d+\\+\\d+\\:\\d+\\-asr\\-(\\w+)?(\\_)?\\.json";
	
	public static final String QUERY_WAV_REGX = "\\d+\\-\\d+\\-\\d+T"
			+ "\\d+\\:\\d+\\:\\d+\\."
			+ "\\d+\\+\\d+\\:\\d+\\-asr\\-(\\w+)?(\\_)?\\.wav";
	
	public static final String QUERY_NLU_JSON_REGX = "\\d+\\-\\d+\\-\\d+T"
			+ "\\d+\\:\\d+\\:\\d+\\."
			+ "\\d+\\+\\d+\\:\\d+\\-asr\\-(\\w+)?(\\_)?\\.nlu\\.json";
	
//	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss.SSSSS";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String INPUT_FILE_TIME_FORMAT = "yyyy-MM-dd'T'HH_mm_ss.SSSSSSSSS";
	
	public static final String PFIVA_INSTANT_FEEDBACK = "pfiva_instant_feedback";
	public static final String PFIVA_CAPTURE_QUERIES_WITHOUT_HOTWORD = "pfiva_capture_queries_without_hotword";
	public static final String PFIVA_DEFAULT_FEEDBACK_QUERY = "pfiva_default_feedback_query";
	public static final String PFIVA_FIREBASE_SERVER_KEY = "pfiva_firebase_server_key";
}
