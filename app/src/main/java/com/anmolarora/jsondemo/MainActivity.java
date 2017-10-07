package com.anmolarora.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("api.openweathermap.org/data/2.5/weather?q=London");
    }

    public class DownloadTask extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... urls) {
            String result= "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                
                String weatherInfo = jsonObject.getString("weather");
                
                Log.i("Weather content", weatherInfo);
                
                JSONArray arr = new JSONArray(weatherInfo);
                
                for (int i = 0; i < arr.length(); i++) {
                    
                    JSONObject jsonPart = arr.getJSONObject(i);
                    
                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));
                    
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
