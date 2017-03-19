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

package com.example.android.popularmovies.injection.component;

import android.app.Application;
import android.content.Context;

import com.example.android.popularmovies.data.DataManager;
import com.example.android.popularmovies.data.MovieDbApiService;
import com.example.android.popularmovies.data.local.DatabaseHelper;
import com.example.android.popularmovies.data.local.SharedPreferencesHelper;
import com.example.android.popularmovies.injection.context.ApplicationContext;
import com.example.android.popularmovies.injection.module.ActivityModule;
import com.example.android.popularmovies.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @see Singleton
 * @see Component
 * @since 1.2.0 2017/03/18
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    @ApplicationContext
    Context context();

    Application application();

    MovieDbApiService movieDbApiService();

    SharedPreferencesHelper sharedPreferencesHelper();

    DataManager dataManager();
}
