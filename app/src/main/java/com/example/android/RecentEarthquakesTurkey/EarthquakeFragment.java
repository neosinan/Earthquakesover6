package com.example.android.RecentEarthquakesTurkey;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.RecentEarthquakesTurkey.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakeFragment extends Fragment {

    //Log tag to determine where the errors occured
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    //Checks if process is failed
    Boolean emptyBoolean=false;

    //Checks If Something went wrong in the app
    Boolean failedBoolean=false;

    //Empty string to in both Async task and main activity
    String jsonResponse = "";

    //Setting our base URL to build over it
    private String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";

    private static String TURKEY = "&minlatitude=36&maxlatitude=42&minlongitude=26&maxlongitude=45";

    public EarthquakeFragment() {
        // Required empty public constructor
    }

    View mview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_earthquake, container, false);
        mview=rootView;

        //Getting date in ISO8601
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentMonth+=1;
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String todayString ="&endtime=" + currentYear + "-" + currentMonth + "-" + currentDay;

        //Calculating Last Three Month
        if(currentMonth==1 || currentMonth==2 || currentMonth==3){
            currentMonth+=9;
            currentYear-=1;
        } else {
            currentMonth-=3;
        }

        //Checks if The sharedpref about Turkey
        if (getBoolean()){
            USGS_REQUEST_URL+=TURKEY;
        }

        String minMagnitude="&minmagnitude="+getPref();//Checks if The sharedpref about magnitude

        //Completing our json string  for turkey for the last 3 months over 3point earthquakes
        String lastMonth  = "&starttime=" + currentYear + "-" + currentMonth + "-" +  currentDay;
        USGS_REQUEST_URL = USGS_REQUEST_URL + lastMonth + todayString + minMagnitude ;

        InternetRequestAsyncTask inte = new InternetRequestAsyncTask();
        inte.execute();


        // Inflate the layout for this fragment
        return rootView;
    }
    //Method to Update UI when necesary
    private void UpdateUI(){
        ProgressBar progressBar =(ProgressBar) mview.findViewById (R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        if (emptyBoolean){
            TextView emptyView = (TextView) mview.findViewById(R.id.empty_list_item);

            if (isNetworkConnected()==false){
                emptyView.setText("Please check Your Internet Connection");
                emptyView.setVisibility(View.VISIBLE);
            }
            emptyView.setVisibility(View.VISIBLE);
        }
        final ArrayList<Earthquake> earthquakes =  QueryUtils.extractEarthquakes(jsonResponse);

        ListView earthquakeListView = (ListView) mview.findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdaptor adapter = new EarthquakeAdaptor(getActivity(), earthquakes);

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

    //This method checks if we connected to internet or not
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private String getPref(){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        String stra= mSharedPreferences.getString("Mag","3");
        return stra;
    }
    private Boolean getBoolean(){
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        Boolean stra= mSharedPreferences.getBoolean("Region",true);
        return stra;
    }

    private class InternetRequestAsyncTask extends AsyncTask<URL, Void, Earthquake> {
        @Override
        protected Earthquake doInBackground(URL... params) {
            URL url = createUrl(USGS_REQUEST_URL);// Completed (1) Create String for Turkey with good input

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                failedBoolean=true;
                Log.e(LOG_TAG,"Error occured in backhround",e);
            }

            if(jsonResponse==""){
                emptyBoolean=true;
            }

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
            if (url==null){
                return null;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                int a =urlConnection.getResponseCode();
                if (a==200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }else {
                    Log.e(LOG_TAG,"Error while making http request:" + a);
                }

            } catch (IOException e) {
                Log.e(LOG_TAG,"Error while making http request",e);
                // Completed: Handle the exception
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
    }


}
