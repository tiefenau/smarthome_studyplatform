package de.pfiva.mobile.voiceassistant;

public interface Constants {

    public static final String DATA_INGESTION_BASE_URL = "http://future-iot.de/data/ingestion/";
    public static final String DATA_INGESTION_CLIENT_TOKEN_ENDPOINT = "client-token";
    public static final String DATA_INGESTION_NLU_DATA_ENDPOINT = "nlu-data";
    public static final String FEEDBACK_RESPONSE_ENDPOINT = "feedback";

    public static final String FEEDBACK_DATA_KEY = "feedbackData";
    public static final String NLUDATA_KEY = "nluData";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YES_RESPONSE = "Yes";

    public static final String MESSAGE_DATA_KEY = "messageData";
    public static final String MESSAGE_RESPONSE_ENDPOINT = "message-response/{messageId}";

    public static final String SURVEY_DATA_KEY = "surveyData";
    public static final String SURVEY_RESPONSE_ENDPOINT = "survey-response/{surveyId}";
}
