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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataManager;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.Video;
import com.example.android.popularmovies.ui.base.BaseActivity;
import com.example.android.popularmovies.ui.core.Constants;
import com.example.android.popularmovies.ui.core.ErrorView;
import com.example.android.popularmovies.ui.detail.MovieDetailActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import icepick.Bundler;
import icepick.State;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/09
 * @see AppCompatActivity
 * @since 1.0.0 2017/02/09
 */
public class MoviesActivity extends BaseActivity implements MovieGridFragment.OnFragmentInteractionListener,
        MovieDetailFragment.OnFragmentInteractionListener {

    // ButterKnife bindings
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.errorView)
    ErrorView mErrorView;

    @Nullable
    @BindView(R.id.fml_movie_detail)
    NestedScrollView mFmlMovieDetail;

    @Nullable
    @BindView(R.id.fba_favourite)
    FloatingActionButton mFbaFavourite;

    @Nullable
    @BindView(R.id.constraint_layout)
    ConstraintLayout mConstraintLayout;

    @Inject
    DataManager mDataManager;

    /**
     * Indicates the current sort option
     * 1. Sort by Most Popular
     * 2. Sort by Top Rated
     * 3. Sort by favourites
     */
    @State
    short mSort = Constants.FAVOURITES;

    /**
     * Two panel design screen for tablets flag
     */
    @State
    boolean mTwoPane;

    /**
     * The current movie to show (only for tablets)
     */
    @State(CustomBundler.class)
    Movie mSelectedMovie = null;

    private MovieGridFragment mMovieGridFragment;
    private MovieDetailFragment mMovieDetailFragment;

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

        // check if twoPane mode is required
        mTwoPane = mFmlMovieDetail != null;

        if (mTwoPane && mSelectedMovie == null) {
            // Hide the view
            mFmlMovieDetail.setVisibility(View.GONE);

            // show a friendly view to indicate the user why the movies are empty
            mErrorView.setErrorMessage(getString(R.string.message_select_movie));
            mErrorView.setVisibility(View.VISIBLE);
        }

        // Add the grid fragment
        mMovieGridFragment = MovieGridFragment.newInstance(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mMovieGridFragment, MovieGridFragment.TAG)
                .commit();

        // Add the detail fragment (tablet)
        if (mFmlMovieDetail != null) {
            mMovieDetailFragment = MovieDetailFragment.newInstance(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail_container, mMovieDetailFragment, MovieDetailFragment.TAG)
                    .commit();

            // Sets the FAB button
            configFab();
        }
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
    protected void onResume() {
        super.onResume();
        if (mMovieGridFragment != null) {
            mMovieGridFragment.loadMovies(mSort);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_order_by_most_popular:
                mSort = Constants.SORT_MOST_POPULAR;
                break;
            case R.id.action_order_by_top_rated:
                mSort = Constants.SORT_TOP_RATED;
                break;
            case R.id.action_show_favourites:
                mSort = Constants.FAVOURITES;
                break;
            default:
                mSort = Constants.FAVOURITES;
                break;
        }

        // Load the movies
        mMovieGridFragment.loadMovies(mSort);

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEmptyResult() {
        // Fallback to a network resource
        Snackbar.make(mConstraintLayout, getString(R.string.message_no_favourites), Snackbar.LENGTH_LONG).show();
        mSort = Constants.SORT_MOST_POPULAR;
        mMovieGridFragment.loadMovies(Constants.SORT_MOST_POPULAR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMovieSelected(Movie movie) {
        // show the detail fragment or go to the detail activity
        mSelectedMovie = movie;
        if (mTwoPane && mMovieDetailFragment != null) {
            mErrorView.setVisibility(View.GONE);
            mFmlMovieDetail.setVisibility(View.VISIBLE);
            mFbaFavourite.setVisibility(View.VISIBLE);
            mMovieDetailFragment.setMovie(movie);
            configFab();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(Movie.class.getSimpleName(), movie);
            startActivity(intent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMoviesLoadError(@StringRes int stringId) {
        // Show a message
        mErrorView.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playYoutubeVideo(Video video) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.key()));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse("http://www.youtube.com/watch?v=" + video.key()));
        }
    }

    @Optional
    @OnClick(R.id.fba_favourite)
    public void onFabClick() {
        //  store the movie using the content provider
        if (mSelectedMovie != null) {
            if (!mDataManager.isFavorite(mSelectedMovie)) {
                mDataManager.addMovieToFavourites(mSelectedMovie);
                // show snackbar
                Snackbar.make(mConstraintLayout, getString(R.string.message_movie_added), Snackbar.LENGTH_LONG).show();
            } else {
                mDataManager.removeFavourite(mSelectedMovie);
                // show snackbar
                Snackbar.make(mConstraintLayout, getString(R.string.message_movie_removed), Snackbar.LENGTH_LONG).show();
            }

            // refresh data grid
            mMovieGridFragment.loadMovies(Constants.FAVOURITES);

            // Change the fab icon
            configFab();
        }
    }

    /**
     *
     */
    private void configFab() {
        if (mFbaFavourite != null && mTwoPane && mSelectedMovie != null) {
            if (mDataManager.isFavorite(mSelectedMovie)) {
                mFbaFavourite.setImageResource(R.drawable.vector_ic_favorite);
            } else {
                mFbaFavourite.setImageResource(R.drawable.vector_ic_favorite_border);
            }
        } else {
            mFmlMovieDetail.setVisibility(View.GONE);
            mFbaFavourite.setVisibility(View.GONE);
        }
    }

    /**
     * Icepick custom bundler to parcel objects
     */
    public static class CustomBundler implements Bundler<Movie> {
        @Override
        public void put(String key, Movie value, Bundle bundle) {
            bundle.putParcelable(key, value);
        }

        @Override
        public Movie get(String key, Bundle bundle) {
            return bundle.getParcelable(key);
        }
    }
}
