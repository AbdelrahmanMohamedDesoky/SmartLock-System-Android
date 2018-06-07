package aou.smartlock;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetMethodDemo extends AsyncTask<String , Void ,String> {
    String server_response;

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                server_response = readStream(urlConnection.getInputStream());
                if(server_response.contains("LOGIN")){
                    Functions.response = server_response;
                }
                else if (server_response.contains("NAME:")){
                    Functions.name = server_response;
                }
                else if (server_response.contains("deviceId:")){
                    Functions.deviceId = server_response;
                }
                else if (server_response.contains("addDeviceId:")){
                    Functions.addDeviceResponse = server_response;
                }
                else if (server_response.contains("OK")){
                    Functions.remoteUnlockResponse = server_response;
                }
            }

        } catch (Exception e) {

        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("Response", "" + server_response);
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}