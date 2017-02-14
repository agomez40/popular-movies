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

package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.ui.detail.MovieDetailActivity;
import com.example.android.popularmovies.util.ErrorView;
import com.example.android.popularmovies.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0.0 2017/02/09
 * @see AppCompatActivity
 * @since 1.0.0 2017/02/09
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener,
        ErrorView.ErrorViewListener {
    /**
     * Grid number of columns
     */
    private static final int NUM_COLUMNS = 2;

    /**
     * The GridView to show movie posters
     */
    private RecyclerView mGridViewMovies;

    /**
     * The custom error view
     */
    private ErrorView mErrorView;

    /**
     * The progress indicator
     */
    private ProgressBar mProgressBar;

    /**
     * The AsyncTask for network IO
     */
    private MovieDatabaseQueryTask mMovieTask;

    /**
     * Indicates the current sort option
     * 0. Latest
     * 1. Sort by Most Popular
     * 2. Sort by Top Rated
     */
    private short mSort = 0;

    /**
     * The current page of the last query to the API
     */
    private int mPage = 0;

    /**
     * The total results
     */
    private int mPages = 0;

    /**
     * The GridView adapter
     */
    private MoviesAdapter mMoviesAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // Get the movies grid view
        mGridViewMovies = (RecyclerView) findViewById(R.id.gv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridViewMovies.setItemAnimator(new DefaultItemAnimator());
        mGridViewMovies.setLayoutManager(gridLayoutManager);
        mGridViewMovies.setHasFixedSize(true);

        mErrorView = (ErrorView) findViewById(R.id.ev_popular_movies);
        mErrorView.setErrorViewListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_movies);

        // Restore the app state
        initUI(savedInstanceState);

        // The adapter instance
        mMoviesAdapter = new MoviesAdapter(this);
        mGridViewMovies.setAdapter(mMoviesAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putShort("sort", mSort);
        savedInstanceState.putInt("page", mPage);
        savedInstanceState.putInt("mPages", mPages);

        if (mMoviesAdapter.getItems() != null) {
            savedInstanceState.putParcelableArrayList("movies", mMoviesAdapter.getItems());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_order_by_most_popular:
                sortByMostPopular();
                break;
            case R.id.action_order_by_top_rated:
                sortByTopRated();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        // Check if the task is running
        if (mMovieTask != null) {
            // Cancel the task or it will leak on activity destroy or rotating the screen
            mMovieTask.cancel(true);
            mMovieTask = null;
        }
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRetryClick() {
        // retry the action on error, fetch the movies
        mErrorView.setVisibility(View.GONE);

        sortByMostPopular();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMovieClick(Movie movie) {
        // Forward to the Detail activity and pass the movie detail
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    /**
     * Init the main UI
     *
     * @param savedInstanceState the saved instance state
     * @since 1.0.0 2017/02/12
     */
    private void initUI(Bundle savedInstanceState) {
        // The UI initial state
        mErrorView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mGridViewMovies.setVisibility(View.VISIBLE);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            /*
             * TODO Restore app state when device rotates from the savedInstanceState using the parcelable
             * instead of calling again the API
             */
            switch (savedInstanceState.getInt("mSort")) {
                case 1:
                    sortByMostPopular();
                    break;
                case 2:
                    sortByTopRated();
                    break;
                default:
                    sortByMostPopular();
                    break;
            }
        } else {
            sortByMostPopular();
        }
    }

    /**
     * Shows or hides the progress indicator
     *
     * @param show {@literal true} to show the progress indicatior, otherwise {@literal false}
     * @since 1.0.0 2017/02/12
     */
    private void showProgressIndicator(boolean show) {
        if (show) {
            // Hide the error view and the grid view
            mGridViewMovies.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mGridViewMovies.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Shows the error view with the provided string message
     *
     * @param stringId the resource string
     * @since 1.0.0 2017/02/13
     */
    private void showError(@StringRes int stringId) {
        mGridViewMovies.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        mErrorView.setErrorMessage(getString(stringId));
        mErrorView.setVisibility(View.VISIBLE);
    }

    /**
     * @since 1.0.0 2017/02/12
     */
    private void sortByMostPopular() {
        mSort = 1;

        // Check if a task is running and cancel it
        if (mMovieTask != null) {
            mMovieTask.cancel(true);
            mMovieTask = null;
        }

        // Query parameters
        // NOTE: Optional params "language" Pass a ISO 639-1 value to display translated data
        Map<String, String> params = new HashMap<>();
        params.put("api_key", getString(R.string.movie_db_api_v3_key));

        try {
            // create the URL and execute the async task
            URL url = NetworkUtil.buildUrl(NetworkUtil.GET_POPULAR_MOVIES, params);
            mMovieTask = new MovieDatabaseQueryTask();
            mMovieTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            showError(R.string.error_no_results);
        }
    }

    /**
     * @since 1.0.0 2017/02/12
     */
    private void sortByTopRated() {
        mSort = 2;

        // Check if a task is running and cancel it
        if (mMovieTask != null) {
            mMovieTask.cancel(true);
            mMovieTask = null;
        }

        // Query parameters
        // NOTE: Optional params "language" Pass a ISO 639-1 value to display translated data
        Map<String, String> params = new HashMap<>();
        params.put("api_key", getString(R.string.movie_db_api_v3_key));

        try {
            // create the URL and execute the async task
            URL url = NetworkUtil.buildUrl(NetworkUtil.GET_TOP_RATED_MOVIES, params);
            mMovieTask = new MovieDatabaseQueryTask();
            mMovieTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            showError(R.string.error_no_results);
        }
    }

    /**
     * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
     * @version 1.0.0 2017/02/12
     * @see AsyncTask
     * @since 1.0.0 2017/02/12
     */
    private class MovieDatabaseQueryTask extends AsyncTask<URL, Void, List<Movie>> {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Check network connection
            if (NetworkUtil.isNetworkConnected(MainActivity.this)) {
                // Hide the GridView and show the progress bar
                showProgressIndicator(true);
            } else {
                // cancel itself
                cancel(true);
                showError(R.string.error_no_network);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<Movie> doInBackground(URL... params) {
            try {
                JSONObject response = NetworkUtil.queryMovieDatabaseAPI(params[0]);

                // Get the current page
                mPage = response.getInt("page");

                // The total pages
                mPages = response.getInt("total_pages");

                Log.d(MovieDatabaseQueryTask.class.getSimpleName(), "Total results: " + response.getInt("total_results"));

                // The movies!
                JSONArray results = response.getJSONArray("results");

                // Parse the movies into our model
                List<Movie> movies = new ArrayList<>(0);

                for (int i = 0; i < results.length(); i++) {
                    movies.add(Movie.newInstance(results.getJSONObject(i)));
                }

                return movies;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            if (movies != null && !movies.isEmpty()) {
                // fill the grid adapter and display the movies
                mMoviesAdapter.setMovies(movies);
                showProgressIndicator(false);
            } else {
                // show error view with no data :(
                showError(R.string.error_no_results);
            }
        }
    }
}
