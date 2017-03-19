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

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Movie Genre entity, as defined by:
 * <a href="https://developers.themoviedb.org/3/movies">TheMovieDB.org API</a>
 *
 * @version 1.2.0 2017/03/18
 * @see Parcelable
 * @since 1.2.0 2017/03/18
 */
@AutoValue
public abstract class Genre implements Parcelable {
    // The Genre id
    public abstract Integer id();

    // The Genre name
    public abstract String name();

    /**
     * Gets the AutoValue_Genre.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/28
     */
    public static Genre.Builder builder() {
        return new AutoValue_Genre.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter<Genre>}
     * @since 1.2.0 2017/03/28
     */
    public static TypeAdapter<Genre> typeAdapter(Gson gson) {
        return new AutoValue_Genre.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection
     * </p>
     * Allows to create new instances of {@link Genre}
     * using a this factory method
     *
     * @since 1.2.0 2017/03/28
     */
    @AutoValue.Builder
    public abstract static class Builder {
        /**
         * Sets the {@link Genre} id
         *
         * @param id the id to set
         * @return the builder instance
         * @since 1.2.0 2017/03/28
         */
        public abstract Genre.Builder setId(Integer id);

        /**
         * Sets the {@link Genre} name
         *
         * @param name the name to set
         * @return the builder instance
         * @since 1.2.0 2017/03/28
         */
        public abstract Genre.Builder setName(String name);

        /**
         * Builds and returns a new instance of {@link Genre}
         *
         * @return the {@link Genre}
         * @since 1.2.0 2017/03/28
         */
        public abstract Genre build();
    }
}
