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
 * Movie Production company entity, as defined by:
 * <a href="https://developers.themoviedb.org/3/movies">TheMovieDB.org API</a>
 *
 * @version 1.2.0 2017/03/18
 * @see Parcelable
 * @see AutoValue
 * @since 1.2.0 2017/03/18
 */
@AutoValue
public abstract class ProductionCompany implements Parcelable {
    // The ProductionCompany id
    public abstract Integer id();

    // The ProductionCompany name
    public abstract String name();

    /**
     * Gets the AutoValue_ProductionCompany.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/28
     */
    public static ProductionCompany.Builder builder() {
        return new AutoValue_ProductionCompany.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <ProductionCompany>}
     * @since 1.2.0 2017/03/28
     */
    public static TypeAdapter<ProductionCompany> typeAdapter(Gson gson) {
        return new AutoValue_ProductionCompany.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection
     * </p>
     * Allows to create new instances of {@link ProductionCompany}
     * using a this factory method
     *
     * @since 1.2.0 2017/03/28
     */
    @AutoValue.Builder
    public abstract static class Builder {
        /**
         * Sets the {@link ProductionCompany} id
         *
         * @param id the id to set
         * @return the builder instance
         * @since 1.2.0 2017/03/28
         */
        public abstract ProductionCompany.Builder setId(Integer id);

        /**
         * Sets the {@link ProductionCompany} name
         *
         * @param name the name to set
         * @return the builder instance
         * @since 1.2.0 2017/03/28
         */
        public abstract ProductionCompany.Builder setName(String name);

        /**
         * Builds and returns a new instance of {@link ProductionCompany}
         *
         * @return the {@link ProductionCompany}
         * @since 1.2.0 2017/03/28
         */
        public abstract ProductionCompany build();
    }
}
