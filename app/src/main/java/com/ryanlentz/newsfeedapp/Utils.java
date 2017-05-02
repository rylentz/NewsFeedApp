package com.ryanlentz.newsfeedapp;

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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for requesting and receiving news from the Guardian.com API
 */
public class Utils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Query the guardian API and return a list of NewArticle objects
     */
    public static List<NewsArticle> fetchNewsArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract and return relevant fields from the JSON response and create a list of 
        // NewsArticle objects
        return extractDataFromJson(jsonResponse);
    }

    /**
     * Extracts the data necessary to build a NewsArticle from the JSONObject and creates an
     * ArrayList of NewsArticles
     *
     * @param newsJson The JSON data as a string
     * @return Returns an ArrayList of NewArticle objects
     */
    private static List<NewsArticle> extractDataFromJson(String newsJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create an empty ArrayList to store NewsArticle objects
        ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJson);

            // Create a JSONObject from the root JSONObject
            JSONObject jsonData = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key "results", which contains the articles
            JSONArray articleArray = jsonData.getJSONArray("results");

            // For each article in the articleArray, create a NewsArticle object
            for (int i = 0; i < articleArray.length(); i++) {
                // Get the article JSONObject at position i in the articleArray
                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Extract the article section from the key "sectionName"
                String section = currentArticle.getString("sectionName");

                // Extract the article date from the key "webPublicationDate"
                String date = currentArticle.getString("webPublicationDate");

                // Extract the article title from the key "webTitle"
                String title = currentArticle.getString("webTitle");

                // Extract the article url from the key "webUrl"
                String url = currentArticle.getString("webUrl");

                // Create a NewsArticle from the data and add it to newsArticles
                newsArticles.add(new NewsArticle(title, section, date, url));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return newsArticles;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
}
