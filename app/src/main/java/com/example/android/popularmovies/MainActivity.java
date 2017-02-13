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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

/**
 * @version 1.0.0 2017/02/09
 * @see AppCompatActivity
 * @since 1.0.0 2017/02/09
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The GridView to show movie posters
     */
    private GridView mGridViewMovies;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the movies grid view
        mGridViewMovies = (GridView) findViewById(R.id.gv_movies);

        // TODO Restore app state when device rotates from the savedInstanceState
        // TODO Create an adapter for the GridView
        // TODO Implement onItemClick for each GridView item and start the MovieDetailActivity
        // TODO pass extra data on the MovieDetailActivity intent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_order_by_most_popular:
                break;
            case R.id.action_order_by_top_rated:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private void sortByMostPopular() {
        // TODO call the API to sort by Most Popular
    }

    /**
     *
     */
    private void sortByTopRated() {
        // TODO call the API to sort by top rated
    }
}
