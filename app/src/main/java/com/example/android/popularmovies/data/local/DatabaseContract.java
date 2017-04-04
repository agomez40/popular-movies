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

package com.example.android.popularmovies.data.local;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Database definition contract.
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.2 2017/04/04
 * @since 1.0.2 2017/04/04
 */
public class DatabaseContract {
    /**
     * The content provider Content Authority
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    /**
     * The base content provider URI
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * The Path to the movies provider
     */
    public static final String PATH_MOVIES = "movies";

    /**
     * The SQLite database name
     */
    public static final String DATABASE_NAME = "popular_movies";

    /**
     * The current Database version
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * Defines the movies table structure.
     *
     * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
     * @version 1.0.2 2017/04/04
     * @see BaseColumns
     * @since 1.0.2 2017/04/04
     */
    public static final class MovieEntry implements BaseColumns {
        /**
         * The content URI
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        /**
         * The Table name
         */
        public static final String TABLE_NAME = "movies";

        /**
         * The content type for cursors with more than 1 item
         */
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /**
         * The movie title
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * The movie original title
         */
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        /**
         * The movie summary or overview
         */
        public static final String COLUMN_OVERVIEW = "overview";

        /**
         * The movie release date
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /**
         * The movie poster URI
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /**
         * The movie popularity
         */
        public static final String COLUMN_POPULARITY = "popularity";

        /**
         * The vote average
         */
        public static final String COLUMN_AVERAGE_VOTE = "vote_average";

        /**
         * The vote count
         */
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        /**
         * Backrop path
         */
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        /**
         * The original language
         */
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";

        /**
         * SQL statement to create the SQLite table
         */
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_ORIGINAL_TITLE + " TEXT, " +
                        COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                        COLUMN_OVERVIEW + " TEXT, " +
                        COLUMN_RELEASE_DATE + " TEXT, " +
                        COLUMN_POSTER_PATH + " TEXT, " +
                        COLUMN_POPULARITY + " REAL, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_AVERAGE_VOTE + " REAL, " +
                        COLUMN_VOTE_COUNT + " INTEGER," +
                        COLUMN_BACKDROP_PATH + " TEXT );";

        /**
         * All columns projection
         */
        public static final String[] ALL_COLUMNS = {
                _ID,
                COLUMN_ORIGINAL_TITLE,
                COLUMN_ORIGINAL_LANGUAGE,
                COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE,
                COLUMN_POSTER_PATH,
                COLUMN_POPULARITY,
                COLUMN_TITLE,
                COLUMN_AVERAGE_VOTE,
                COLUMN_VOTE_COUNT,
                COLUMN_BACKDROP_PATH
        };
    }
}
