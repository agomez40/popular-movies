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

package com.example.android.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.data.local.SharedPrefKeys;
import com.example.android.popularmovies.injection.component.ApplicationComponent;
import com.example.android.popularmovies.injection.component.DaggerApplicationComponent;
import com.example.android.popularmovies.injection.module.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Main Application class, initializes the Application modules and components.
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @see Application
 * @since 1.2.0 2017/03/18
 */
public class PopularMoviesApp extends Application {

    /**
     * The application component singleton
     */
    ApplicationComponent mApplicationComponent;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Configure timber's debug tree
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(SharedPrefKeys.API_KEY, getString(R.string.movie_db_api_v3_key));
        editor.apply();
    }

    /**
     * Gets the {@link ApplicationComponent}
     *
     * @return The {@link ApplicationComponent} instance
     * @since 1.2.0 2017/03/18
     */
    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return mApplicationComponent;
    }

    public static PopularMoviesApp getApplication(Context context) {
        return (PopularMoviesApp) context.getApplicationContext();
    }
}
