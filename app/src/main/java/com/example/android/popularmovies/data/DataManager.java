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

package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.local.DatabaseContract;
import com.example.android.popularmovies.data.local.SharedPrefKeys;
import com.example.android.popularmovies.data.local.SharedPreferencesHelper;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.MovieCollection;
import com.example.android.popularmovies.data.model.ReviewCollection;
import com.example.android.popularmovies.data.model.TrailersCollection;
import com.example.android.popularmovies.injection.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @see Singleton
 * @since 1.2.0 2017/03/18
 */
@Singleton
public class DataManager {
    /**
     * A helper to manage shared preferences
     */
    private final SharedPreferencesHelper mSharedPreferencesHelper;

    /**
     * The Retrofit REST service
     */
    private final MovieDbApiService mMovieDbApiService;

    /**
     * The application context
     */
    private final Context mContext;

    /**
     * Injects the service and the helpers using dagger2 dependency injection
     *
     * @param sharedPreferencesHelper The {@link SharedPreferencesHelper} to inject
     * @param movieDbApiHelper        The {@link MovieDbApiService} to inject
     * @see Inject
     * @since 1.2.0 2017/03/18
     */
    @Inject
    public DataManager(@ApplicationContext Context context, SharedPreferencesHelper sharedPreferencesHelper,
                       MovieDbApiService movieDbApiHelper) {
        this.mContext = context;
        this.mSharedPreferencesHelper = sharedPreferencesHelper;
        this.mMovieDbApiService = movieDbApiHelper;
    }

    /**
     * Gets the {@link SharedPreferencesHelper}
     *
     * @return the singleton instance of {@link SharedPreferencesHelper}
     * @since 1.2.0 2017/03/18
     */
    public SharedPreferencesHelper getPreferencesHelper() {
        return mSharedPreferencesHelper;
    }

    /**
     * Get a list of the current popular movies on TMDb. This list updates daily.
     *
     * @param page     The page to fetch
     * @param language The language locale
     * @return A Observable object, which wraps the REST API result
     * @since 1.2.0 2017/03/18
     */
    public Observable<MovieCollection> getPopularMovies(@Nullable Integer page,
                                                        @Nullable String language) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getPopularMovies(apiKey, page, language);
    }

    /**
     * Get the top rated movies on TMDb.
     *
     * @param page     The page to fetch
     * @param language The language locale
     * @return A Observable object, which wraps the REST API result
     * @since 1.2.0 2017/03/18
     */
    public Observable<MovieCollection> getTopRatedMovies(@Nullable Integer page,
                                                         @Nullable String language) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getTopRatedMovies(apiKey, page, language);
    }

    /**
     * Gets the movie reviews from the REST API.
     *
     * @param movieId The movie id
     * @return A Observable object, which wraps the REST API result
     * @since 1.2.0 2017/03/18
     */
    public Observable<ReviewCollection> getReviews(Long movieId) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getMovieReviews(movieId, apiKey);
    }

    /**
     * Gets the movie trailers from the REST API.
     *
     * @param movieId The movie id
     * @return A Observable object, which wraps the REST API result
     * @since 1.2.0 2017/03/18
     */
    public Observable<TrailersCollection> getTrailers(Long movieId) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getMovieTrailers(movieId, apiKey);
    }

    /**
     * Gets the favourite movies.
     *
     * @param page The current page to fetch
     * @return A Observable object, which wraps the SQLite result
     * @since 1.2.0 2017/03/18
     */
    public Observable<MovieCollection> getFavourites(final int page) {
        return Observable.defer(new Callable<ObservableSource<? extends MovieCollection>>() {
            @Override
            public ObservableSource<? extends MovieCollection> call() throws Exception {
                // Do some long running operation
                Cursor cursor = null;
                MovieCollection collection;
                try {
                    cursor = mContext.getContentResolver().query(DatabaseContract.MovieEntry.CONTENT_URI,
                            DatabaseContract.MovieEntry.ALL_COLUMNS,
                            null,
                            null,
                            null);

                    List<Movie> results = new ArrayList<>(0);

                    while (cursor != null && cursor.moveToNext()) {
                        results.add(Movie.fromCursor(cursor));
                    }

                    collection = MovieCollection.builder()
                            .setPage(page)
                            .setResults(results)
                            .setTotal_pages(1)
                            .setTotal_results(results.size())
                            .build();

                    return Observable.just(collection);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }

    /**
     * Adds a movie to the favourite collection.
     *
     * @param movie The movie to add
     * @since 1.2.0 2017/03/18
     */
    public void addMovieToFavourites(Movie movie) {
        mContext.getContentResolver().insert(DatabaseContract.MovieEntry.CONTENT_URI,
                Movie.toContentValues(movie));
    }

    /**
     * Removes a movie to the favourite collection.
     *
     * @param movie The movie to delete
     * @since 1.2.0 2017/03/18
     */
    public void removeFavourite(Movie movie) {
        mContext.getContentResolver().delete(
                DatabaseContract.MovieEntry.CONTENT_URI,
                DatabaseContract.MovieEntry._ID + " = " + movie.id(),
                null
        );
    }

    /**
     * Checks if a movie is stored on the favourites collection.
     *
     * @param movie The movie to check
     * @return {@literal true} if the movie is a favourite, otherwise {@literal false}
     * @since 1.2.0 2017/03/18
     */
    public boolean isFavorite(Movie movie) {
        boolean favorite = false;
        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.MovieEntry.CONTENT_URI,
                null,
                DatabaseContract.MovieEntry._ID + " = " + movie.id(),
                null,
                null
        );
        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();
        }
        return favorite;
    }
}
