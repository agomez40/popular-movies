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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataManager;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.MovieCollection;
import com.example.android.popularmovies.ui.base.BaseActivity;
import com.example.android.popularmovies.ui.core.ErrorView;
import com.example.android.popularmovies.ui.detail.MovieDetailActivity;
import com.example.android.popularmovies.util.TheMovieDbUtil;
import com.example.android.popularmovies.util.ViewUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/09
 * @see AppCompatActivity
 * @since 1.0.0 2017/02/09
 */
public class MoviesActivity extends BaseActivity implements MoviesAdapter.MovieItemClickListener,
        ErrorView.ErrorViewListener {

    // ButterKnife bindings
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.gv_movies)
    RecyclerView mRvMovies;
    @BindView(R.id.pb_loading_movies)
    ProgressBar mPbLoadingMovies;
    @BindView(R.id.ev_popular_movies)
    ErrorView mEvPopularMovies;

    /**
     * Indicates the current sort option
     * 1. Sort by Most Popular
     * 2. Sort by Top Rated
     */
    private short mSort = 0;

    /**
     * The GridView adapter
     */
    private MoviesAdapter mMoviesAdapter;

    /**
     * The current movie collection to display
     */
    private MovieCollection mMovieCollection;

    @Inject
    DataManager mDataManager;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Inject the dependencies
        activityComponent().inject(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        mRvMovies.setHasFixedSize(true);
        mRvMovies.setItemViewCacheSize(20);
        mRvMovies.setDrawingCacheEnabled(true);
        mRvMovies.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mEvPopularMovies.setErrorViewListener(this);

        // Initial view state
        mEvPopularMovies.setVisibility(View.GONE);
        mPbLoadingMovies.setVisibility(View.GONE);
        mRvMovies.setVisibility(View.VISIBLE);

        // Config the grid
        RecyclerView.LayoutManager layoutManager = ViewUtil.configGridLayout(this);
        mRvMovies.setLayoutManager(layoutManager);

        // The initial adapter, should get data from the stored state on future updates
        mMoviesAdapter = new MoviesAdapter(this);
        mMoviesAdapter.setMovies(new ArrayList<Movie>(0));
        mRvMovies.setAdapter(mMoviesAdapter);

        if (savedInstanceState != null) {
            mSort = savedInstanceState.getShort("sort");

            mMovieCollection = savedInstanceState.getParcelable("movieCollection");

            if (mMovieCollection != null) {
                mMoviesAdapter.setMovies(mMovieCollection.results());
            }
        } else {
            // Fetch the movies from the network
            getMovies(TheMovieDbUtil.SORT_MOST_POPULAR);
        }
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
        savedInstanceState.putParcelable("movieCollection", mMovieCollection);

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
                getMovies(TheMovieDbUtil.SORT_MOST_POPULAR);
                break;
            case R.id.action_order_by_top_rated:
                getMovies(TheMovieDbUtil.SORT_TOP_RATED);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Config the grid
        RecyclerView.LayoutManager layoutManager = ViewUtil.configGridLayout(this);
        mRvMovies.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter(this);
        mMoviesAdapter.setMovies(new ArrayList<Movie>(0));
        mRvMovies.setAdapter(mMoviesAdapter);

        // XXX should get this from the bundle
        getMovies(mSort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRetryClick() {
        // retry the action on error, fetch the movies
        mEvPopularMovies.setVisibility(View.GONE);

        getMovies(mSort);
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
     * Shows or hides the progress indicator
     *
     * @param show {@literal true} to show the progress indicatior, otherwise {@literal false}
     * @since 1.0.0 2017/02/12
     */
    private void showProgressIndicator(boolean show) {
        if (show) {
            // Hide the error view and the grid view
            mRvMovies.setVisibility(View.GONE);
            mEvPopularMovies.setVisibility(View.GONE);
            mPbLoadingMovies.setVisibility(View.VISIBLE);
        } else {
            mRvMovies.setVisibility(View.VISIBLE);
            mPbLoadingMovies.setVisibility(View.GONE);
        }
    }

    /**
     * Shows the error view with the provided string message
     *
     * @param stringId the resource string
     * @since 1.0.0 2017/02/13
     */
    private void showError(@StringRes int stringId) {
        mRvMovies.setVisibility(View.GONE);
        mPbLoadingMovies.setVisibility(View.GONE);

        mEvPopularMovies.setErrorMessage(getString(stringId));
        mEvPopularMovies.setVisibility(View.VISIBLE);
    }

    /**
     * @param sort The sorting option
     * @since 1.0.0 2017/02/18
     */
    private void getMovies(short sort) {
        mDataManager.getPopularMovies(1, null);
    }
}
