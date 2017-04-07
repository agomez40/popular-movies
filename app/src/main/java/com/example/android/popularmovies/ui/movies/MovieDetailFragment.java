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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataManager;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.ReviewCollection;
import com.example.android.popularmovies.data.model.TrailersCollection;
import com.example.android.popularmovies.data.model.Video;
import com.example.android.popularmovies.ui.base.BaseActivity;
import com.example.android.popularmovies.ui.base.BaseFragment;
import com.example.android.popularmovies.ui.base.MovieTrailerAdapter;
import com.example.android.popularmovies.ui.base.ReviewsAdapter;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/04/05
 * @see BaseFragment
 * @since 1.2.0 2017/04/05
 */
public class MovieDetailFragment extends BaseFragment implements MovieTrailerAdapter.ItemClickListener {
    /**
     * Fragment TAG
     */
    public static final String TAG = MovieDetailFragment.class.getSimpleName();
    @BindView(R.id.iv_movie_poster)
    ImageView mIvMoviePoster;
    @BindView(R.id.tv_review)
    TextView mTvReview;
    @BindView(R.id.tv_duration)
    TextView mTvDuration;
    @BindView(R.id.tv_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_label_overview)
    TextView mTvLabelOverview;
    @BindView(R.id.tv_overview)
    TextView mTvOverview;
    @BindView(R.id.tv_label_trailers)
    TextView mTvLabelTrailers;
    @BindView(R.id.pager_trailers)
    RecyclerView mPagerTrailers;
    @BindView(R.id.tv_label_reviews)
    TextView mTvLabelReviews;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView mRvMovieReviews;
    @Nullable
    @BindView(R.id.tv_movie_title)
    TextView mTvMovieTitle;
    @BindView(R.id.cv_trailers)
    CardView mCvTrailers;
    @BindView(R.id.cv_reviews)
    CardView mCvReviews;
    Unbinder unbinder;
    /**
     * Inject the data manager using dagger2
     */
    @Inject
    DataManager mDataManager;
    /**
     * Fragment interaction listener
     */
    private OnFragmentInteractionListener mListener;
    // The recycler view adapter
    private ReviewsAdapter mReviewsAdapter;
    // Trailers adapter
    private MovieTrailerAdapter mMovieTrailerAdapter;

    /**
     * Constructor
     *
     * @since 1.2.0 2017/04/05
     */
    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieDetailFragment.
     * @since 1.2.0 2017/04/05
     */
    public static MovieDetailFragment newInstance(OnFragmentInteractionListener listener) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.mListener = listener;
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // at this point we can inject the dependencies
        ((BaseActivity) getActivity()).activityComponent().injectFragment(this);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Sets the selected movie to display.
     *
     * @param movie The movie to set
     * @since 1.2.0 2017/04/07
     */
    public void setMovie(Movie movie) {
        // Re the UI
        mCvReviews.setVisibility(View.GONE);
        mCvTrailers.setVisibility(View.GONE);

        // Use Picasso to load the image into the view
        Picasso.with(mIvMoviePoster.getContext())
                .load("http://image.tmdb.org/t/p/w780/" + movie.backdrop_path())
                .placeholder(R.drawable.vector_movie_placeholder)
                .error(R.drawable.vector_movie_placeholder)
                .into(mIvMoviePoster);

        // Format the release date to get only the year
        mTvReleaseDate.setText(movie.release_date());

        // Format the votes to show avg/10
        mTvReview.setText(String.format(Locale.getDefault(), "%.1f", movie.vote_average()).concat("/10"));
        mTvOverview.setText(movie.overview());
        mTvReleaseDate.setText(movie.release_date());

        // Load the movie trailers
        getMovieTrailers(movie);

        mMovieTrailerAdapter = new MovieTrailerAdapter(getActivity(), this);

        mPagerTrailers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mPagerTrailers.setHasFixedSize(true);
        mPagerTrailers.setItemAnimator(new DefaultItemAnimator());
        mPagerTrailers.setFocusable(false);
        mPagerTrailers.setAdapter(mMovieTrailerAdapter);

        // Load the movie reviews
        getMovieReviews(movie);

        mReviewsAdapter = new ReviewsAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvMovieReviews.setLayoutManager(linearLayoutManager);
        mRvMovieReviews.setHasFixedSize(true);
        mRvMovieReviews.setItemAnimator(new DefaultItemAnimator());
        mRvMovieReviews.setAdapter(mReviewsAdapter);
        mRvMovieReviews.setFocusable(false);

        if (mTvMovieTitle != null) {
            mTvMovieTitle.setText(movie.title());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVideoClick(Video video) {
        if (mListener != null) {
            mListener.playYoutubeVideo(video);
        }
    }

    /**
     * @param movie The movie
     * @since 1.0.0 2017/02/14
     */
    private void getMovieTrailers(Movie movie) {
        mDataManager.getTrailers(movie.id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TrailersCollection>() {

                    @Override
                    public void onNext(TrailersCollection value) {
                        Timber.i("Get Trailers DisposableObserver onNext.");
                        // set the adapter data
                        mMovieTrailerAdapter.setVideos(value.results());

                        // Show the card
                        mCvTrailers.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getMessage());
                        // hide the card
                        mCvTrailers.setVisibility(View.GONE);
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

                            // Show the card
                            mCvReviews.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, e.getMessage());
                            mCvTrailers.setVisibility(View.VISIBLE);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        void playYoutubeVideo(Video video);
    }
}
