package com.satyajeet.earthquakereport;


import android.text.TextUtils;
import android.util.Log;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static ArrayList<EarthquakeDetails> fetchEarthquakeData(String requestURL){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createURL(requestURL);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<EarthquakeDetails> earthquakeDetails = extractFeatureFromJson(jsonResponse);
        return  earthquakeDetails;
    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link EarthquakeDetails} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<EarthquakeDetails> extractFeatureFromJson(String earthquakeJSON){

        if(TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthquakeDetails> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject root = new JSONObject(earthquakeJSON);
            JSONArray features = root.getJSONArray("features");

            for(int i=0; i<features.length(); i++){
                JSONObject currentEarthquake = features.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                String primaryLocation, locationOffset;


                double mag = properties.getDouble("mag");//
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String fmag = decimalFormat.format(mag);

                // Place Computations
                String place  = properties.getString("place");//
                String lplace = place.toLowerCase();
                int idx = lplace.indexOf("of");
                if(!lplace.contains("of")){
                    primaryLocation = "Near the ";
                    locationOffset = lplace.substring(idx+1, place.length());
                } else {
                    primaryLocation = lplace.substring(0,idx+2);
                    locationOffset = lplace.substring(idx+2, place.length());
                }


                // Time computations
                String time = properties.getString("time");//
                long utime = Long.parseLong(time);
                Date dateObject = new Date(utime);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("d, MM, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);

                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm a");
                String timeToDisplay = timeFormatter.format(dateObject);

                String url = properties.getString("url");




                EarthquakeDetails earthquakeDetails = new EarthquakeDetails(fmag, primaryLocation.toUpperCase().trim(), locationOffset.toUpperCase().trim(), dateToDisplay, timeToDisplay, url);
                earthquakes.add(earthquakeDetails);

            }
            return earthquakes;

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return null;
    }


    private static URL createURL(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL requestURl) throws IOException {

        String jsonResponse = "";
        if (requestURl == null){
            return  jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) requestURl.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: "+ urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in retrieving the earthqauke JSON results: ", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line !=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
