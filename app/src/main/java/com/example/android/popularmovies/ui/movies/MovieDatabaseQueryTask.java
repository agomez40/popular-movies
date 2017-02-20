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

package com.example.android.popularmovies.ui.movies;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieCollection;
import com.example.android.popularmovies.ui.core.AsyncTaskCompleteListener;
import com.example.android.popularmovies.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.1 2017/02/19
 * @see AsyncTask
 * @since 1.0.0 2017/02/12
 * // TODO replace this task using Retrofit
 */
class MovieDatabaseQueryTask extends AsyncTask<URL, Void, MovieCollection> {

    private static final String TAG = MovieDatabaseQueryTask.class.getSimpleName();
    private Activity mContext;
    private AsyncTaskCompleteListener<MovieCollection> mListener;

    /**
     * Constructor
     *
     * @since 1.0.0 2017/02/19
     */
    MovieDatabaseQueryTask(@NonNull Activity context, @NonNull AsyncTaskCompleteListener<MovieCollection> listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Check network connection
        if (NetworkUtil.isNetworkConnected(mContext)) {
            // Hide the GridView and show the progress bar
            mListener.onTaskRunning();
        } else {
            // cancel itself
            cancel(true);
            mListener.onTaskError(R.string.error_no_network);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MovieCollection doInBackground(URL... params) {
        try {
            JSONObject response = NetworkUtil.queryMovieDatabaseAPI(params[0]);

            // The MovieCollection
            return MovieCollection.newInstance(response);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(MovieCollection movies) {
        super.onPostExecute(movies);

        if (movies != null) {
            // fill the grid adapter and display the movies
            mListener.onTaskComplete(movies);
        } else {
            // show error view with no data :(
            mListener.onTaskError(R.string.error_no_results);
        }
    }
}