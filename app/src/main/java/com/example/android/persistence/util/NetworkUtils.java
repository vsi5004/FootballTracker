package com.example.android.persistence.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String OPENLIGADB_BASE_URL = "https://www.openligadb.de/api/";

    final public static String QUERY_AVAILABLE_TEAMS = "getavailableteams/bl1/2017";
    final public static String QUERY_ALL_GAMES = "getmatchdata/bl1/2017";
    final public static String QUERY_CURRENT_MATCHDAY_GAMES = "getmatchdata/bl1";
    final public static String QUERY_GOAL_SCORERS = "getgoalgetters/bl1/2017";

    public static String runQuery(String query){
        try {
            return getResponseFromHttpUrl(buildUrl(query));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("NETWORK","Unable to access the URL");
        }
        return null;
    }

    public static URL buildUrl(String query) {
        Uri builtUri = Uri.parse(OPENLIGADB_BASE_URL).buildUpon().appendPath(query).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e("URL","Couldn't build URL for query: "+query);
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}