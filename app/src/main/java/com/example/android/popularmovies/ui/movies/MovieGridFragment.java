package com.example.android.popularmovies.ui.movies;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.DataManager;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.MovieCollection;
import com.example.android.popularmovies.ui.base.BaseActivity;
import com.example.android.popularmovies.ui.base.BaseFragment;
import com.example.android.popularmovies.ui.base.MoviesAdapter;
import com.example.android.popularmovies.ui.core.Constants;
import com.example.android.popularmovies.ui.core.ErrorView;
import com.example.android.popularmovies.util.ExceptionParser;
import com.example.android.popularmovies.util.ViewUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Bundler;
import icepick.State;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieGridFragment extends BaseFragment implements MoviesAdapter.MovieItemClickListener {

    public static final String TAG = MovieGridFragment.class.getSimpleName();

    @BindView(R.id.gv_movies)
    RecyclerView mRvMovies;
    @BindView(R.id.pb_loading_movies)
    ProgressBar mPbLoadingMovies;
    @BindView(R.id.ev_popular_movies)
    ErrorView mRvPopularMovies;
    Unbinder unbinder;

    /**
     * Inject the data manager using dagger2
     */
    @Inject
    DataManager mDataManager;

    /**
     * The GridView adapter
     */
    private MoviesAdapter mMoviesAdapter;
    /**
     * RxJava subscription to fetch data
     */
    private Disposable mDisposableSubscription;

    private OnFragmentInteractionListener mListener;

    /**
     * The current movie collection to display
     */
    @State(CustomBundler.class)
    MovieCollection mMovieCollection;

    /**
     * The page to fetch from the API
     * defaults to 1
     */
    @State
    int mPage = 1;

    @State
    short mSort = Constants.FAVOURITES;

    /**
     *
     */
    public MovieGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieGridFragment.
     */
    public static MovieGridFragment newInstance(OnFragmentInteractionListener listener) {
        MovieGridFragment movieGridFragment = new MovieGridFragment();
        movieGridFragment.mListener = listener;
        return movieGridFragment;
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
        void onEmptyResult();

        void onMovieSelected(Movie movie);

        void onMoviesLoadError(@StringRes int stringId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Config the grid
        RecyclerView.LayoutManager layoutManager = ViewUtil.configGridLayout(view.getContext());
        mRvMovies.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter(view.getContext(), this);
        mRvMovies.setAdapter(mMoviesAdapter);

        if (savedInstanceState != null) {
            if (mMovieCollection != null) {
                setAdapterData(mMovieCollection.results());
            }
        }

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Due to a bug on the grid redraw recreate the adapter
        mMoviesAdapter = new MoviesAdapter(getContext(), this);
        mRvMovies.setAdapter(mMoviesAdapter);

        // Config the grid
        RecyclerView.LayoutManager layoutManager = ViewUtil.configGridLayout(getContext());
        mRvMovies.setLayoutManager(layoutManager);

        // should get this from the bundle using icepick
        loadMovies(mSort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // at this point we can inject the dependencies
        ((BaseActivity) getActivity()).activityComponent().injectFragment(this);

        // Check the listener
        if (context instanceof MovieDetailFragment.OnFragmentInteractionListener) {
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
    public void onMovieClick(Movie movie) {
        // Forward to the activity, the parent should add the detail view or create an intent to details
        if (mListener != null) {
            mListener.onMovieSelected(movie);
        }
    }

    /**
     * @param sort
     */
    public void loadMovies(short sort) {
        if (mDisposableSubscription != null) {
            mDisposableSubscription.dispose();
        }

        switch (sort) {
            case Constants.SORT_MOST_POPULAR:
                mDisposableSubscription = mDataManager.getPopularMovies(mPage, null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newDisposableObserver());
                break;
            case Constants.SORT_TOP_RATED:
                mDisposableSubscription = mDataManager.getTopRatedMovies(mPage, null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newDisposableObserver());
                break;
            default:
                mDisposableSubscription = mDataManager.getFavourites(mPage)
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
        showProgressIndicator(true);
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

                showProgressIndicator(false);
                if (mListener != null) {
                    mListener.onMoviesLoadError(errorStringId);
                }
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
        if (movies.size() > 0) {
            mMoviesAdapter.setMovies(movies);
            showProgressIndicator(false);
        } else {
            mListener.onEmptyResult();
        }
    }

    /**
     * Shows or hides the progress indicator
     *
     * @param show {@literal true} to show the progress indicator, otherwise {@literal false}
     * @since 1.0.0 2017/02/12
     */
    private void showProgressIndicator(boolean show) {
        if (show) {
            // Hide the error view and the grid view
            mRvMovies.setVisibility(View.GONE);
            mPbLoadingMovies.setVisibility(View.VISIBLE);
        } else {
            mRvMovies.setVisibility(View.VISIBLE);
            mPbLoadingMovies.setVisibility(View.GONE);
        }
    }

    /**
     * Icepick custom bundler to parcel objects
     */
    public static class CustomBundler implements Bundler<MovieCollection> {
        @Override
        public void put(String key, MovieCollection value, Bundle bundle) {
            bundle.putParcelable(key, value);
        }

        @Override
        public MovieCollection get(String key, Bundle bundle) {
            return bundle.getParcelable(key);
        }
    }
}
