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

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.local.DatabaseContract;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

import timber.log.Timber;

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
     * @since 1.2.0 2017/03/18
     */
    public static TypeAdapter<Movie> typeAdapter(Gson gson) {
        return new AutoValue_Movie.GsonTypeAdapter(gson);
    }

    /**
     * Parses the object to a ContentValue
     *
     * @return the parsed object
     * @since 1.2.0 2017/03/18
     */
    public static ContentValues toContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MovieEntry._ID, movie.id());
        values.put(DatabaseContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.original_title());
        values.put(DatabaseContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.original_language());
        values.put(DatabaseContract.MovieEntry.COLUMN_OVERVIEW, movie.overview());
        values.put(DatabaseContract.MovieEntry.COLUMN_RELEASE_DATE, movie.release_date());
        values.put(DatabaseContract.MovieEntry.COLUMN_POSTER_PATH, movie.poster_path());
        values.put(DatabaseContract.MovieEntry.COLUMN_POPULARITY, movie.popularity());
        values.put(DatabaseContract.MovieEntry.COLUMN_TITLE, movie.title());
        values.put(DatabaseContract.MovieEntry.COLUMN_AVERAGE_VOTE, movie.vote_average());
        values.put(DatabaseContract.MovieEntry.COLUMN_VOTE_COUNT, movie.vote_count());
        values.put(DatabaseContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.backdrop_path());
        return values;
    }

    /**
     * Parses a cursor and returns a new movie instance
     *
     * @param cursor The cursor to parse.
     * @return The movie instance
     * @since 1.2.0 2017/04/04
     */
    public static Movie fromCursor(Cursor cursor) {
        try {
            return Movie.builder()
                    .setId(cursor.getLong(cursor.getColumnIndex(DatabaseContract.MovieEntry._ID)))
                    .setTitle(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_TITLE)))
                    .setOriginal_title(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_ORIGINAL_TITLE)))
                    .setOriginal_language(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)))
                    .setOverview(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_OVERVIEW)))
                    .setRelease_date(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_RELEASE_DATE)))
                    .setPoster_path(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_POSTER_PATH)))
                    .setPopularity(cursor.getFloat(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_POPULARITY)))
                    .setVote_average(cursor.getFloat(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_AVERAGE_VOTE)))
                    .setBackdrop_path(cursor.getString(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_BACKDROP_PATH)))
                    .setVote_count(cursor.getInt(cursor.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_VOTE_COUNT)))
                    .build();
        } catch (Exception e) {
            Timber.e(e, e.getMessage());
            return null;
        }
    }

    public abstract String poster_path();

    public abstract String overview();

    public abstract String release_date();

    public abstract Long id();

    public abstract String original_title();

    public abstract String original_language();

    public abstract String title();

    public abstract String backdrop_path();

    public abstract Float popularity();

    public abstract Integer vote_count();

    public abstract Float vote_average();

    // optional fields used by details
    public abstract
    @Nullable
    Boolean adult();

    public abstract
    @Nullable
    Boolean video();

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

    public abstract
    @Nullable
    Boolean favourite();

    /**
     * Class Builder dagger 2 injection.
     * </p>
     * Allows to create new instances of {@link Movie}
     * using this factory method.
     *
     * @since 1.2.0 2017/03/18
     */
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Movie.Builder setPoster_path(String poster_path);

        public abstract Movie.Builder setAdult(@Nullable Boolean adult);

        public abstract Movie.Builder setOverview(String overview);

        public abstract Movie.Builder setRelease_date(String release_date);

        public abstract Movie.Builder setId(Long id);

        public abstract Movie.Builder setOriginal_title(String original_title);

        public abstract Movie.Builder setOriginal_language(String original_language);

        public abstract Movie.Builder setTitle(String title);

        public abstract Movie.Builder setBackdrop_path(String backdrop_path);

        public abstract Movie.Builder setPopularity(Float popularity);

        public abstract Movie.Builder setVote_count(Integer vote_count);

        public abstract Movie.Builder setVideo(@Nullable Boolean video);

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
