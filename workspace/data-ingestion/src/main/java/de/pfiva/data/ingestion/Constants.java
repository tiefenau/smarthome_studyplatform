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
	
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss.SSSSS";
}
