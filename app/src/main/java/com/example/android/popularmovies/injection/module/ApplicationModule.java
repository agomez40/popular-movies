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

package com.example.android.popularmovies.injection.module;

import android.app.Application;
import android.content.Context;

import com.example.android.popularmovies.data.MovieDbApiService;
import com.example.android.popularmovies.injection.context.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides application level dependency injection
 * using Dagger2.
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @since 1.2.0 2017/03/18
 */
@Module
public class ApplicationModule {
    private final Application mApplication;

    /**
     * Constructor
     *
     * @param application The application instance
     * @since 1.2.0 2017/03/18
     */
    public ApplicationModule(Application application) {
        this.mApplication = application;
    }

    /**
     * Provides an Application instance
     *
     * @return The application instance
     * @since 1.2.0 2017/03/18
     */
    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    /**
     *
     * @return
     */
    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    /**
     *
     * @return
     */
    @Provides
    @Singleton
    MovieDbApiService provideMovieDbApiService() {
        return MovieDbApiService.Creator.newMovieDbApiService();
    }
}
