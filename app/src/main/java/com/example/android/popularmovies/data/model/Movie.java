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

package com.example.android.popularmovies.data.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Movie entity, as defined by:
 * <a href="https://developers.themoviedb.org/3/movies">TheMovieDB.org API</a>
 * <p>
 * Optional fields are annotated by @Nullable, so Gson can ignore them when using the
 * TypeAdapterFactory.
 *
 * @author Luis Alberto Gómez Rodrígurez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @see Parcelable
 * @since 1.2.0 2017/03/18
 */
@AutoValue
public abstract class Movie implements Parcelable {
    public abstract String poster_path();

    public abstract Boolean adult();

    public abstract String overview();

    public abstract String release_date();

    public abstract List<Integer> genre_ids();

    public abstract Integer id();

    public abstract String original_title();

    public abstract String original_language();

    public abstract String title();

    public abstract String backdrop_path();

    public abstract Float popularity();

    public abstract Integer vote_count();

    public abstract Boolean video();

    public abstract Float vote_average();

    // fields used by details
    public abstract
    @Nullable
    Integer budget();

    public abstract
    @Nullable
    List<Genre> genres();

    public abstract
    @Nullable
    String homepage();

    public abstract
    @Nullable
    String imdb_id();

    public abstract
    @Nullable
    List<ProductionCompany> production_companies();

    public abstract
    @Nullable
    List<ProductionCountry> production_countries();

    public abstract
    @Nullable
    Integer revenue();

    public abstract
    @Nullable
    Integer runtime();

    public abstract
    @Nullable
    List<SpokenLanguage> spoken_languages();

    public abstract
    @Nullable
    String status();

    public abstract
    @Nullable
    String tagline();

    public abstract @Nullable Boolean favourite();


    /**
     * Gets the AutoValue_Movie.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/18
     */
    public static Movie.Builder builder() {
        return new AutoValue_Movie.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <Card>}
     * @since 5.0.0 2016/12/14
     */
    public static TypeAdapter<Movie> typeAdapter(Gson gson) {
        return new AutoValue_Movie.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection.
     * </p>
     * Allows to create new instances of {@link Movie}
     * using this factory method.
     *
     * @since 5.0.0 2016/12/14
     */
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Movie.Builder setPoster_path(String poster_path);

        public abstract Movie.Builder setAdult(Boolean adult);

        public abstract Movie.Builder setOverview(String overview);

        public abstract Movie.Builder setRelease_date(String release_date);

        public abstract Movie.Builder setGenre_ids(List<Integer> genre_ids);

        public abstract Movie.Builder setId(Integer id);

        public abstract Movie.Builder setOriginal_title(String original_title);

        public abstract Movie.Builder setOriginal_language(String original_language);

        public abstract Movie.Builder setTitle(String title);

        public abstract Movie.Builder setBackdrop_path(String backdrop_path);

        public abstract Movie.Builder setPopularity(Float popularity);

        public abstract Movie.Builder setVote_count(Integer vote_count);

        public abstract Movie.Builder setVideo(Boolean video);

        public abstract Movie.Builder setVote_average(Float vote_average);

        public abstract Movie.Builder setBudget(@Nullable Integer budget);

        public abstract Movie.Builder setGenres(@Nullable List<Genre> genres);

        public abstract Movie.Builder setHomepage(@Nullable String homepage);

        public abstract Movie.Builder setImdb_id(@Nullable String imdb_id);

        public abstract Movie.Builder setProduction_companies(@Nullable List<ProductionCompany> production_companies);

        public abstract Movie.Builder setProduction_countries(@Nullable List<ProductionCountry> production_countries);

        public abstract Movie.Builder setRevenue(@Nullable Integer revenue);

        public abstract Movie.Builder setRuntime(@Nullable Integer runtime);

        public abstract Movie.Builder setSpoken_languages(@Nullable List<SpokenLanguage> spoken_languages);

        public abstract Movie.Builder setStatus(@Nullable String status);

        public abstract Movie.Builder setTagline(@Nullable String tagline);

        public abstract Movie.Builder setFavourite(@Nullable Boolean favourite);

        public abstract Movie build();
    }
}
