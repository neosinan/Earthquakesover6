package com.example.android.RecentEarthquakesTurkey;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject rootJsonObject = new JSONObject(jsonResponse);
            JSONArray featuresJson = rootJsonObject.getJSONArray("features");

            for (int i = 0; i< featuresJson.length();i++){
                JSONObject currentObject = featuresJson.getJSONObject(i);
                JSONObject properties = currentObject.getJSONObject("properties");

                String magString = properties.getString("mag");
                Double magDouble = Double.parseDouble(magString);
                DecimalFormat decFormatter = new DecimalFormat("0.0");
                String output = decFormatter.format(magDouble);
                magDouble = Double.parseDouble(output);
                String URLString = properties.getString("url");


                String locString = properties.getString("place");

                String part1;
                String part2;

                if (locString.contains("of")) {
                    String[] parts = locString.split("of");
                  part1 = " " + parts[0] + " of";
                  part2 = parts[1];
                }else {
                    String[] parts;
                    part1 = locString;
                    part2 = "";
                }

                String  dateString = properties.getString("time");
                Long dateLong = Long.parseLong(dateString);
                String mDate = "";

                // Create a DateFormatter object for displaying date in specified format.
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

                // Create a calendar object that will convert the date and time value in milliseconds to date.
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateLong);
                mDate = formatter.format(calendar.getTime());

                earthquakes.add(new Earthquake(magDouble.toString(),part1,part2,mDate,URLString));
            }

            // (Completed): Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}