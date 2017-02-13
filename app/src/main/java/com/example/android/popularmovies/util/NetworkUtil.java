/*
 * Copyright 2017 Luis Alberto Gómez Rodríguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Basic network utils
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/09
 * @since 1.0.0 2017/02/09
 */
public class NetworkUtil {
    /**
     * The movie database base API URI
     */
    private static final String MOVIE_DATABASE_BASE_URL =
            "https://api.themoviedb.org/3/movies/";

    /**
     * https://developers.themoviedb.org/3/movies/get-top-rated-movies
     */
    public static final String GET_TOP_RATED_MOVIES = "top_rated";

    /**
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     */
    public static final String GET_POPULAR_MOVIES = "popular";

    /**
     * https://developers.themoviedb.org/3/movies/get-latest-movie
     */
    public static final String GET_LATEST_MOVIES = "latest";

    /**
     * Builds the URL used to query The Movie Database.
     *
     * @param path   The keyword for the API method or resource.
     * @param params The query string parameters
     * @return The URL to use to query the Movie Database API.
     */
    public static URL buildUrl(String path, Map<String, String> params) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(MOVIE_DATABASE_BASE_URL).buildUpon();
        builder.appendPath(path);

        if (params != null && !params.isEmpty()) {
            // Append the params as query string
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
                Log.d(NetworkUtil.class.getSimpleName(), "Added query Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }

        return new URL(builder.build().toString());
    }

    /**
     * Performs an HTTP GET using the provided url
     *
     * @param url the url
     * @return the API response as a {@link JSONObject}
     * @throws IOException   if the request could not be executed due to cancellation, a connectivity
     *                       problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                       remote server accepted the request before the failure.
     * @throws JSONException if the parse fails or doesn't yield a {@code
     *                       JSONObject}.
     * @see OkHttpClient
     * @since 1.0.0 2017/02/12
     */
    public static JSONObject queryMovieDatabaseAPI(URL url) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        JSONObject jsonResponse = new JSONObject(response.body().toString());

        // Close the stream, or it will leak memory
        response.close();

        return jsonResponse;
    }

    /**
     * Checks the device's network connection and availability
     *
     * @param context the application context
     * @return {@literal true} where there's an available network, otherwise {@literal false}
     * @since 1.0.0 2017/02/09
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Constructor
     * <p/>
     * This class can't be instantiated directly, creating an
     * instance of the class using {@code new NetworkUtil} will throw
     * an {@link AssertionError}
     * <p/>
     * All members and methods are static, and MUST be called
     * using it's static form i.e. {@code NetworkUtil.isNetworkConnected(context)}.
     *
     * @since 1.0.0 2017/02/09
     */
    private NetworkUtil() {
        /* throws an new AssertionError */
        throw new AssertionError();
    }
}
