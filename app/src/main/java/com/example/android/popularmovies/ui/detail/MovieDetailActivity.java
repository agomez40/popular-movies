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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Luis Alberto Gómez Rodríguez (alberto.gomez@cargomovil.com)
 * @version 1.0.1 2017/02/19
 * @see AppCompatActivity
 * @since 1.0.0 2017/02/14
 */
public class MovieDetailActivity extends AppCompatActivity {

    // ButterKnife view bindings
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_movie_poster)
    ImageView mIvMoviePoster;
    @BindView(R.id.tv_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_vote_average)
    TextView mTvVoteAverage;
    @BindView(R.id.tv_synopsis)
    TextView mTvSynopsis;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get the extras and set the movie title
        if (!getIntent().getExtras().isEmpty()) {
            Movie movie = getIntent().getParcelableExtra("movie");
            initUI(movie);
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
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .placeholder(R.drawable.vector_movie_placeholder)
                .error(R.drawable.vector_movie_error)
                .into(mIvMoviePoster);

        mTvTitle.setText(movie.getTitle());

        // Format the release date to get only the year
        mTvReleaseDate.setText(movie.getReleaseDate().substring(0, 4));

        // Format the votes to show avg/10
        mTvVoteAverage.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()).concat("/10"));
        mTvSynopsis.setText(movie.getOverview());
    }
}
