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

import com.example.android.popularmovies.data.model.ReviewCollection;
import com.example.android.popularmovies.data.model.Movie;
import com.example.android.popularmovies.data.model.MovieCollection;
import com.example.android.popularmovies.data.model.TrailersCollection;
import com.example.android.popularmovies.util.AppGsonTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @since 1.2.0 2017/03/18
 */
public interface MovieDbApiService {
    String BASE_API_HOST =
            "https://api.themoviedb.org/3/movie/";

    /**
     * Get a list of the current popular movies on TMDb. This list updates daily.
     *
     * @param apiKey   The api key
     * @param page     The page to fetch
     * @param language The language ICU locale
     * @return
     * @since 1.2.0 2017/03/18
     */
    @GET("popular")
    Observable<MovieCollection> getPopularMovies(@Query("api_key") String apiKey,
                                                 @Query("page") Integer page,
                                                 @Query("language") String language);

    /**
     * Get the top rated movies on TMDb.
     *
     * @param apiKey   The api key
     * @param page     The page to fetch
     * @param language The language ICU locale
     * @return
     * @since 1.2.0 2017/03/18
     */
    @GET("top_rated")
    Observable<MovieCollection> getTopRatedMovies(@Query("api_key") String apiKey,
                                                  @Query("page") Integer page,
                                                  @Query("language") String language);

    /**
     * @param apiKey  The api key
     * @param movieId The movie id
     * @return
     * @since 1.2.0 2017/03/18
     */
    @GET("{movie_id}/reviews")
    Observable<ReviewCollection> getMovieReviews(@Path("movie_id") Integer movieId,
                                                 @Query("api_key") String apiKey);

    /**
     * @param apiKey  The api key
     * @param movieId The movie id
     * @return
     * @since 1.2.0 2017/03/18
     */
    @GET("{movie_id}/videos")
    Observable<TrailersCollection> getMovieTrailers(@Path("movie_id") Integer movieId,
                                                    @Query("api_key") String apiKey);

    /**
     * Helper class that sets up a new services
     *
     * @since 1.2.0 2017/03/18
     */
    class Creator {
        /**
         * Factory method
         *
         * @return The service instance
         * @since 1.2.0 2017/03/18
         */
        public static MovieDbApiService newMovieDbApiService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(AppGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieDbApiService.BASE_API_HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(MovieDbApiService.class);
        }
    }
}
