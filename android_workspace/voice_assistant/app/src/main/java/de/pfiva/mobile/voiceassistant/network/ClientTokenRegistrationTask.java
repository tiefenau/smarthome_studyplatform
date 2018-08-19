package de.pfiva.mobile.voiceassistant.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import de.pfiva.mobile.voiceassistant.network.model.ClientToken;

public class ClientTokenRegistrationTask  extends AsyncTask<ClientToken, Void, Void> {

    private static final String TAG = "ClientRegistrationTask";

    @Override
    protected Void doInBackground(ClientToken... clientTokens) {
        ClientToken clientToken = clientTokens[0];

        Log.i(TAG, "URL for forwarding client token: [" + clientToken.getUrl());
        executeRequest(clientToken);
        return null;
    }

    private void executeRequest(ClientToken clientToken) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(clientToken.getUrl());
            String urlParameters = getParamsString(clientToken.getParameters());

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            con.setRequestProperty("Content-Language", "en-US");

            con.setUseCaches(false);
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(urlParameters);
            out.close();

            //Get response
            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error while executing client token request");
        } catch (IOException e) {
            Log.e(TAG, "Error while executing client token request");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String getParamsString(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        Log.i(TAG, "Parameters for client registration [" + resultString + "]'");
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
