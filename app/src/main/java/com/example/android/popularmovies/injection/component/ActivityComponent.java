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

import com.example.android.popularmovies.injection.module.ActivityModule;
import com.example.android.popularmovies.ui.detail.MovieDetailActivity;
import com.example.android.popularmovies.ui.movies.DetailFragment;
import com.example.android.popularmovies.ui.movies.MovieGridFragment;
import com.example.android.popularmovies.ui.movies.MoviesActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 *
 * @author Luis Alberto Gómez Rodríguez (alberto.gomez@cargomovil.com)
 * @version 1.2.0 2017/03/18
 * @see ActivityModule
 * @since 1.2.0 2017/03/18
 */
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    /*Inject here the activities / fragments*/
    void inject(MoviesActivity moviesActivity);

    void inject(MovieDetailActivity movieDetailActivity);
}
