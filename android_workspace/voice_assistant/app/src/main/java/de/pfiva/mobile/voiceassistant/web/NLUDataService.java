package de.pfiva.mobile.voiceassistant.web;

import java.util.List;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.notification.ClientToken;
import de.pfiva.mobile.voiceassistant.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NLUDataService {

    @GET(Constants.DATA_INGESTION_NLU_DATA_ENDPOINT)
    public Call<List<NLUData>> getNLUData();

    @POST(Constants.FEEDBACK_RESPONSE_ENDPOINT)
    public Call<Boolean> saveFeedbackResponse(@Body Feedback feedback);

    @POST(Constants.DATA_INGESTION_CLIENT_TOKEN_ENDPOINT)
    public Call<Void> saveClientToken(@Body ClientToken clientToken);
}
