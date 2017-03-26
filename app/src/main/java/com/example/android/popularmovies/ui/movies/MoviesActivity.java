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
import com.example.android.popularmovies.util.ExceptionParser;
import com.example.android.popularmovies.util.ViewUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
     * The page to fetch from the API
     * defaults to 1
     */
    private int mPage = 1;

    /**
     * Two panel design screen for tablets flag
     */
    private boolean mTwoPane;

    /**
     * The GridView adapter
     */
    private MoviesAdapter mMoviesAdapter;

    /**
     * The current movie collection to display
     */
    private MovieCollection mMovieCollection;

    /**
     * RxJava subscription to fetch data
     */
    private Disposable mDisposableSubscription;

    /**
     * Sort by most popular flag
     */
    public final static short SORT_MOST_POPULAR = 1;

    /**
     * Sort by top rated flag
     */
    public final static short SORT_TOP_RATED = 2;

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

        /*if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }*/

        mEvPopularMovies.setErrorViewListener(this);

        // Config the grid
        RecyclerView.LayoutManager layoutManager = ViewUtil.configGridLayout(this);
        mRvMovies.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter(this, this);
        mRvMovies.setAdapter(mMoviesAdapter);

        if (savedInstanceState != null) {
            mSort = savedInstanceState.getShort("sort");

            mMovieCollection = savedInstanceState.getParcelable("movieCollection");

            if (mMovieCollection != null) {
                setAdapterData(mMovieCollection.results());
            }
        } else {
            // Fetch the movies from the network
            getMovies(SORT_MOST_POPULAR);
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
                getMovies(SORT_MOST_POPULAR);
                break;
            case R.id.action_order_by_top_rated:
                getMovies(SORT_TOP_RATED);
                break;
            case R.id.action_show_favourites:
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
        // Due to a bug on the grid redraw recreate the adapter
        mMoviesAdapter = new MoviesAdapter(this, this);
        mRvMovies.setAdapter(mMoviesAdapter);

        // Config the grid
        RecyclerView.LayoutManager layoutManager = ViewUtil.configGridLayout(this);
        mRvMovies.setLayoutManager(layoutManager);

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
            mEvPopularMovies.setVisibility(View.GONE);
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
     * TODO implement infinite scroll
     *
     * @param sort The sorting option
     * @since 1.0.0 2017/02/18
     */
    private void getMovies(short sort) {
        if (mDisposableSubscription != null) {
            mDisposableSubscription.dispose();
        }

        showProgressIndicator(true);

        switch (sort) {
            case SORT_MOST_POPULAR:
                mDisposableSubscription = mDataManager.getPopularMovies(mPage, null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newDisposableObserver());
                break;
            case SORT_TOP_RATED:
                mDisposableSubscription = mDataManager.getTopRatedMovies(mPage, null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newDisposableObserver());
                break;
            default:
                mDisposableSubscription = mDataManager.getPopularMovies(mPage, null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newDisposableObserver());
                break;
        }

    }

    /**
     * @return
     */
    private DisposableObserver<MovieCollection> newDisposableObserver() {
        return new DisposableObserver<MovieCollection>() {
            @Override
            public void onNext(MovieCollection value) {
                Timber.i("Get Movies DisposableObserver onNext.");
                setAdapterData(value.results());
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, e.getMessage());

                // Parse the error to show a user friendly message
                int errorStringId = ExceptionParser.parseException((Exception) e);
                showError(errorStringId);
            }

            @Override
            public void onComplete() {
                Timber.i("Get Popular Movies DisposableObserver completed.");
            }
        };
    }

    /**
     * @param movies
     */
    private void setAdapterData(List<Movie> movies) {
        mMoviesAdapter.setMovies(movies);
        showProgressIndicator(false);
    }
}
