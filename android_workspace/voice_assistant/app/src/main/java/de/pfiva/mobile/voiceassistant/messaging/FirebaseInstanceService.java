package de.pfiva.mobile.voiceassistant.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.network.ClientTokenRegistrationTask;
import de.pfiva.mobile.voiceassistant.network.model.ClientToken;
import de.pfiva.mobile.voiceassistant.utilities.MobileVoiceAssistantUtilities;

public class FirebaseInstanceService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseInstanceService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        ClientToken clientToken = new ClientToken();
        clientToken.setUrl(Constants.DATA_INGESTION_BASE_URL
                + Constants.DATA_INGESTION_CLIENT_TOKEN_ENDPOINT);


        String macAddress = MobileVoiceAssistantUtilities.getMacAddress();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("clientName", MobileVoiceAssistantUtilities.getMacAddress());
        parameters.put("token", refreshedToken);

        Log.i(TAG, "Forwarding clientName [" + macAddress + "]," +
                " and token [" + refreshedToken + "].");

        clientToken.setParameters(parameters);

        new ClientTokenRegistrationTask().execute(clientToken);
    }
}
