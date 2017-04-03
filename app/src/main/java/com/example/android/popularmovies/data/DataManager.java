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

import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.local.SharedPrefKeys;
import com.example.android.popularmovies.data.local.SharedPreferencesHelper;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.MovieCollection;
import com.example.android.popularmovies.data.model.ReviewCollection;
import com.example.android.popularmovies.data.model.TrailersCollection;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

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
     * Injects the service and the helpers using dagger2 dependency injection
     *
     * @param sharedPreferencesHelper The {@link SharedPreferencesHelper} to inject
     * @param movieDbApiHelper        The {@link MovieDbApiService} to inject
     * @see Inject
     * @since 1.2.0 2017/03/18
     */
    @Inject
    public DataManager(SharedPreferencesHelper sharedPreferencesHelper,
                       MovieDbApiService movieDbApiHelper) {
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
     * @param page The page to fetch
     * @param language The language locale
     * @return
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
     * @param page The page to fetch
     * @param language The language locale
     * @return
     * @since 1.2.0 2017/03/18
     */
    public Observable<MovieCollection> getTopRatedMovies(@Nullable Integer page,
                                                         @Nullable String language) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getTopRatedMovies(apiKey, page, language);
    }

    /**
     *
     * @param movieId The movie id
     * @return
     * @since 1.2.0 2017/03/18
     */
    public Observable<ReviewCollection> getReviews(Integer movieId) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getMovieReviews(movieId, apiKey);
    }

    /**
     *
     * @param movieId The movie id
     * @return
     * @since 1.2.0 2017/03/18
     */
    public Observable<TrailersCollection> getTrailers(Integer movieId) {
        String apiKey = (String) mSharedPreferencesHelper.getEntry(SharedPrefKeys.API_KEY, String.class);
        return mMovieDbApiService.getMovieTrailers(movieId, apiKey);
    }
}
