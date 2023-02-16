package com.example.earthquake;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

//    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Earthquake> fetchEarthquakeData(String mUrl) {

        // Create URL object
        URL url = createUrl(mUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return new ArrayList<>(Objects.requireNonNull(QueryUtils.extractFeatureFromJson(jsonResponse)));

    }

    static List<Earthquake> extractFeatureFromJson(String jsonResponse) {
        try {
            if(TextUtils.isEmpty(jsonResponse)){
                return null;
            }
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");

            // If there are results in the features array
            if (featureArray.length() > 0) {
                // Extract out the first feature (which is an earthquake)

                List<Earthquake> earthquakes = new ArrayList<>();

                for (int i = 0; i < featureArray.length(); i++) {
                    JSONObject currentFeature = featureArray.getJSONObject(i);
                    JSONObject properties = currentFeature.getJSONObject("properties");
                    JSONArray geometry = currentFeature.getJSONObject("geometry").getJSONArray("coordinates");

                    double magnitude = properties.getDouble("mag");
                    String location = properties.getString("place");
                    long time = properties.getLong("time");
                    String url = properties.getString("url");
                    long latitude = geometry.getLong(0);
                    long longitude = geometry.getLong(1);
                    long depth = geometry.getLong(2);





                    Earthquake earthquake = new Earthquake(magnitude, location, time, url,latitude,longitude,depth);

                    // Add the new {@link Earthquake} to the list of earthquakes.

                    earthquakes.add(earthquake);
                }


                return earthquakes;
            }
        } catch (JSONException e) {
//          todo  Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }



    static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
//       todo     Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));
            }

        } catch (IOException e) {
            Log.e(LOG_TAG,e.getMessage());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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
