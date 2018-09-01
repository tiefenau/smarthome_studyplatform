package de.pfiva.wear.voiceassistant.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import de.pfiva.data.model.notification.ClientToken;
import de.pfiva.wear.voiceassistant.network.RetrofitClientInstance;
import de.pfiva.wear.voiceassistant.service.NLUDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if(refreshedToken != null && !refreshedToken.trim().isEmpty()) {
            ClientToken clientToken = new ClientToken();
            clientToken.setClientName("android-wear");
            clientToken.setToken(refreshedToken);
            Log.i(TAG, "Sending token for client [" +
                    clientToken.getClientName() + "], as [" +
                    clientToken.getToken() + "]");

            NLUDataService nluDataService = RetrofitClientInstance.getRetrofitInstance()
                    .create(NLUDataService.class);

            Call<Void> saveClientTokenCall = nluDataService.saveClientToken(clientToken);
            saveClientTokenCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.i(TAG, "Client token saved successfully.");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.i(TAG, "Error while saving client token.");
                }
            });
        }
    }
}
