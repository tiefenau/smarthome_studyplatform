package de.pfiva.wear.voiceassistant.service;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.notification.ClientToken;
import de.pfiva.wear.voiceassistant.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NLUDataService {

    @POST(Constants.DATA_INGESTION_CLIENT_TOKEN_ENDPOINT)
    public Call<Void> saveClientToken(@Body ClientToken clientToken);

    @POST(Constants.FEEDBACK_RESPONSE_ENDPOINT)
    public Call<Boolean> saveFeedbackResponse(@Body Feedback feedback);
}
