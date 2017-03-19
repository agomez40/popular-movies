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

import java.util.List;

/**
 * Simple entity to represent an API Movie collection response
 *
 * @version 1.0.1 2016/02/19
 * @see Parcelable
 * @since 1.0.1 2017/02/19
 */
@AutoValue
public abstract class MovieCollection implements Parcelable {
    public abstract Integer page();
    public abstract List<Movie> results();
    public abstract Integer total_results();
    public abstract Integer total_pages();

    /**
     * Gets the AutoValue_MovieCollection.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/28
     */
    public static MovieCollection.Builder builder() {
        return new AutoValue_MovieCollection.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <MovieCollection>}
     * @since 1.2.0 2017/03/28
     */
    public static TypeAdapter<MovieCollection> typeAdapter(Gson gson) {
        return new AutoValue_MovieCollection.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection
     * </p>
     * Allows to create new instances of {@link MovieCollection}
     * using a this factory method
     *
     * @since 1.2.0 2017/03/28
     */
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract MovieCollection.Builder setPage(Integer page);
        public abstract MovieCollection.Builder setResults(List<Movie> results);
        public abstract MovieCollection.Builder setTotal_results(Integer total_results);
        public abstract MovieCollection.Builder setTotal_pages(Integer total_pages);
        public abstract MovieCollection build();
    }
}
