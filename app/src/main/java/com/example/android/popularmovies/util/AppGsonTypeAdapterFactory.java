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

package com.example.android.popularmovies.util;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Gson type adapter factory. Allows us to create model classes using
 * AutoValue and Retrofit.
 *
 * @author
 * @see GsonTypeAdapterFactory
 * @see TypeAdapterFactory
 */
@GsonTypeAdapterFactory
public abstract class AppGsonTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * Creates a new TypeAdapterFactory.
     *
     * @return the factory
     */
    public static TypeAdapterFactory create() {
        return new AutoValueGson_AppGsonTypeAdapterFactory();
    }
}
