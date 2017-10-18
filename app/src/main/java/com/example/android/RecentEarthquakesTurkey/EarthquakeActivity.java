/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.RecentEarthquakesTurkey;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    Double magDouble;

    String jsonResponse = "";

    private String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minlatitude=36&maxlatitude=42&minlongitude=26&maxlongitude=42";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        currentMonth+=1;

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        String todayString ="&endtime=" + currentYear + "-" + currentMonth + "-" + currentDay;

        if(currentMonth==1 || currentMonth==2 || currentMonth==3){
            currentMonth+=9;
            currentYear-=1;
        } else {
            currentMonth-=3;
        }

        String lastMonth  = "&starttime=" + currentYear + "-" + currentMonth + "-" +  currentDay;

        USGS_REQUEST_URL = USGS_REQUEST_URL + lastMonth + todayString + "&minmagnitude=3";
        // Create a fake list of earthquake locations.

        InternetRequestAsyncTask task = new InternetRequestAsyncTask();
        task.execute();

        UpdateUI();
    }

    private void UpdateUI(){
        final ArrayList<Earthquake> earthquakes =  QueryUtils.extractEarthquakes(jsonResponse);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdaptor adapter = new EarthquakeAdaptor(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake = earthquakes.get(position);

                String URLString = earthquake.getmURL();
                //URLString = Earthquake.getmURL(position));
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URLString));
                startActivity(i);
            }
        });
    }

    private class InternetRequestAsyncTask extends AsyncTask<URL, Void, Earthquake> {
        @Override
        protected Earthquake doInBackground(URL... params) {
            URL url = createUrl(USGS_REQUEST_URL);// ToDo (1) Create String for Turkey with good input

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            //Earthquake earthquake1 ;
            //ToDo(2) Re write the code to send proper earthquake instance or a link
            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return null;
        }


        protected void onPostExecute(Earthquake earthquake) {

            UpdateUI();
            if (earthquake == null) {
                return;
            }


        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         *We already have these as Instance of Earthquake class
         */
        private Earthquake extractFeatureFromJson(String earthquakeJSON) {

            return null;
        }
    }


}
