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

package com.example.android.popularmovies.ui.detail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataManager;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.ReviewCollection;
import com.example.android.popularmovies.data.model.TrailersCollection;
import com.example.android.popularmovies.ui.base.BaseActivity;
import com.example.android.popularmovies.ui.base.ReviewsAdapter;
import com.example.android.popularmovies.util.ExceptionParser;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * @author Luis Alberto Gómez Rodríguez (alberto.gomez@cargomovil.com)
 * @version 1.0.1 2017/02/19
 * @see AppCompatActivity
 * @since 1.0.0 2017/02/14
 */
public class MovieDetailActivity extends BaseActivity {

    // ButterKnife view bindings
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_review)
    TextView mTvReview;
    @BindView(R.id.tv_duration)
    TextView mTvDuration;
    @BindView(R.id.tv_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_overview)
    TextView mTvOverview;
    @BindView(R.id.pager_trailers)
    RecyclerView mPagerTrailers;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView mRvMovieReviews;
    @BindView(R.id.fab_movie_detail)
    FloatingActionButton mFabMovieDetail;
    @BindView(R.id.iv_movie_poster)
    ImageView mIvMoviePoster;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    ImageView mAppBarImage;
    // The recycler view adapter
    ReviewsAdapter mReviewsAdapter;
    // Injects the manager using dagger2
    @Inject
    DataManager mDataManager;
    // The current movie detail
    private Movie mMovie;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        // Inject the dependencies
        activityComponent().inject(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            mAppBarImage = (ImageView) findViewById(R.id.app_bar_image);
        }

        // Get the extras and set the movie title
        if (!getIntent().getExtras().isEmpty()) {
            mMovie = getIntent().getParcelableExtra("movie");
            initUI(mMovie);
        }
    }

    /**
     * Init the UI
     *
     * @param movie the movie
     * @since 1.0.0 2017/02/14
     */
    private void initUI(Movie movie) {
        // Use Picasso to load the image into the view
        Picasso.with(mIvMoviePoster.getContext())
                .load("http://image.tmdb.org/t/p/w500/" + movie.poster_path())
                .placeholder(R.drawable.vector_movie_placeholder)
                .error(R.drawable.vector_movie_placeholder)
                .into(mIvMoviePoster);

        mCollapsingToolbar.setTitle(movie.title());

        if (mAppBarImage != null) {
            // load the parallax photo
            Picasso.with(mIvMoviePoster.getContext())
                    .load("http://image.tmdb.org/t/p/w500/" + movie.backdrop_path())
                    .placeholder(R.color.colorPrimary)
                    .error(R.color.colorPrimary)
                    .fit()
                    .into(mAppBarImage);
        }

        // Format the release date to get only the year
        mTvReleaseDate.setText(movie.release_date());

        // Format the votes to show avg/10
        mTvReview.setText(String.format(Locale.getDefault(), "%.1f", movie.vote_average()).concat("/10"));
        mTvOverview.setText(movie.overview());
        mTvReleaseDate.setText(movie.release_date());

        // Load the movie trailers
        // getMovieTrailers(movie);

        mPagerTrailers.setLayoutManager(new

                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPagerTrailers.setHasFixedSize(true);
        mPagerTrailers.setItemAnimator(new DefaultItemAnimator());
        mPagerTrailers.setFocusable(false);

        // TODO set the adapter
        // Load the movie reviews
        getMovieReviews(movie);

        mReviewsAdapter = new ReviewsAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvMovieReviews.setLayoutManager(linearLayoutManager);
        mRvMovieReviews.setHasFixedSize(true);
        mRvMovieReviews.setItemAnimator(new DefaultItemAnimator());
        mRvMovieReviews.setAdapter(mReviewsAdapter);
        mRvMovieReviews.setFocusable(false);

        if (mDataManager.isFavorite(mMovie)) {
            mFabMovieDetail.setImageResource(R.drawable.vector_ic_favorite);
        } else {
            mFabMovieDetail.setImageResource(R.drawable.vector_ic_favorite_border);
        }
    }

    @OnClick(R.id.fab_movie_detail)
    public void onViewClicked() {
        // add it to the favourites database
        if (!mDataManager.isFavorite(mMovie)) {
            mDataManager.addMovieToFavourites(mMovie);
            mFabMovieDetail.setImageResource(R.drawable.vector_ic_favorite);
        } else {
            mDataManager.removeFavourite(mMovie);
            mFabMovieDetail.setImageResource(R.drawable.vector_ic_favorite_border);
        }
    }

    /**
     * @param movie The movie
     * @since 1.0.0 2017/02/14
     */
    private void getMovieTrailers(Movie movie) {
        // TODO show the loading indicator inside the trailers card
        mDataManager.getTrailers(movie.id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TrailersCollection>() {

                    @Override
                    public void onNext(TrailersCollection value) {
                        Timber.i("Get Trailers DisposableObserver onNext.");
                        // TODO set the adapter data

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getMessage());
                        // Parse the error to show a user friendly message
                        int errorStringId = ExceptionParser.parseException((Exception) e);

                        // TODO show the error and hide the card
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Get Trailers DisposableObserver completed.");
                    }
                });
    }

    /**
     * @param movie The movie
     * @since 1.0.0 2017/02/14
     */
    private void getMovieReviews(Movie movie) {
        // TODO show the loading indicator inside the reviews card
        try {
            mDataManager.getReviews(movie.id())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<ReviewCollection>() {

                        @Override
                        public void onNext(ReviewCollection value) {
                            Timber.i("Get Reviews DisposableObserver onNext.");
                            // set the adapter data
                            mReviewsAdapter.setReviews(value.results());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, e.getMessage());
                            // Parse the error to show a user friendly message
                            int errorStringId = ExceptionParser.parseException((Exception) e);

                            // TODO show the error and hide the card
                        }

                        @Override
                        public void onComplete() {
                            Timber.i("Get Reviews DisposableObserver completed.");
                        }
                    });
        } catch (Exception e) {
            Timber.e(e, e.getMessage());
        }
    }
}
