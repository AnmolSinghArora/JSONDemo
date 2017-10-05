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

    //Async allows to run code in background i.e. different thread
    public class DownloadTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls){ //Varargs and not an array//

            String result="";
            URL url;
            HttpURLConnection urlConnection = null; // kind of like a browser

            try{

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();//Like opening browser window

                InputStream in = urlConnection.getInputStream(); //stream to hold input of data as it comes in

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read(); // reads contents of url

                while(data != -1){ // data loops through all characters

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            }
            catch(Exception e){


                e.printStackTrace(); // to give error details like not proper address

                return "Failed";
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather Content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for(int i=0; i<arr.length(); i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();

        //String result = task.execute(" ").get();
        AsyncTask<String, Void, String> result = null;
        try {
            result = task.execute("api.openweathermap.org/data/2.5/weather?q=London,uk");
        } catch (Exception e) {

            e.printStackTrace();

        }


    }
}