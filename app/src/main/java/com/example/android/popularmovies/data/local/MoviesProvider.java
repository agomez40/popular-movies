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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Application local storage ContentProvider implementation.
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.2 2017/04/04
 * @see ContentProvider
 * @since 1.0.2 2017/04/04
 */
public class MoviesProvider extends ContentProvider {

    /**
     * The code for the Favourites movies
     */
    public static final int FAVOURITES = 1983;

    /**
     * The UriMatcher
     */
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    /**
     * Error while inserting rows
     */
    private static final String FAILED_TO_INSERT_ROW_INTO = "Failed to insert row into ";

    /**
     * The SQLite database helper
     */
    private SQLiteHelper mSqLiteHelper;

    /**
     * @return the UriMatcher
     * @see UriMatcher
     * @since 1.0.2 2017/04/04
     */
    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, DatabaseContract.PATH_MOVIES + "/", FAVOURITES);

        return uriMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreate() {
        // Create the db
        mSqLiteHelper = new SQLiteHelper(getContext());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Normally we would have a lot of queries here, but the app only implements one (for now)
        // since the application only stores the favourite movies and does not perform any sync
        // with other network resources (like storing the most popular movies or the highest rated)
        int match = URI_MATCHER.match(uri);

        // The output cursor
        Cursor cursor;

        switch (match) {
            case FAVOURITES:
                cursor = getFavouriteMovies(DatabaseContract.MovieEntry.ALL_COLUMNS, selection,
                        selectionArgs, sortOrder);
                break;
            default:
                return null;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Normally we would have a lot of URis here, but the app only implements one (for now)
        // since the application only stores the favourite movies and does not perform any sync
        // with other network resources (like storing the most popular movies or the highest rated)
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case FAVOURITES:
                return DatabaseContract.MovieEntry.CONTENT_DIR_TYPE;
            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = URI_MATCHER.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVOURITES:
                returnUri = insertFavourite(uri, values);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = URI_MATCHER.match(uri);

        int rowsDeleted;

        switch (match) {
            case FAVOURITES:
                rowsDeleted = deleteFavourite(selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Could not delete the record, Unsupported URI: " + uri);
        }

        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = URI_MATCHER.match(uri);

        int rowsUpdated;
        switch (match) {
            case FAVOURITES:
                rowsUpdated = updateFavourite(values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Could not update the record, Unsupported update URI: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /**
     * Gets all the user favourite movies.
     *
     * @param projection    the columns to fetch
     * @param selection     the where clause
     * @param selectionArgs the where arguments
     * @param sortOrder     sort order
     * @return a cursor over the result set
     * @since 1.0.2 2017/04/04
     */
    private Cursor getFavouriteMovies(String[] projection, String selection,
                                      String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        sqLiteQueryBuilder.setTables(DatabaseContract.MovieEntry.TABLE_NAME);

        return sqLiteQueryBuilder.query(mSqLiteHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
    }

    /**
     * Inserts a favourite movie.
     *
     * @param uri    The URI
     * @param values The content values to insert
     * @return the URI to notify
     * @throws SQLException if the record could not be inserted
     * @since 1.0.2 2017/04/04
     */
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    private Uri insertFavourite(Uri uri, ContentValues values) throws SQLException {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();

        try {
            long inserted = db.insert(DatabaseContract.MovieEntry.TABLE_NAME, null, values);

            if (inserted > 0) {
                return DatabaseContract.MovieEntry.CONTENT_URI;
            } else {
                throw new SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
            }
        } finally {
            db.close();
        }
    }

    /**
     * Deletes a favourite movie.
     *
     * @param selection     the where clause
     * @param selectionArgs the where arguments
     * @return the deleted row count
     * @throws SQLException if there is an error while deleting the record
     * @since 1.0.2 2017/04/04
     */
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    private int deleteFavourite(String selection, String[] selectionArgs) throws SQLException {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();

        try {
            return db.delete(DatabaseContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        } finally {
            db.close();
        }
    }

    /**
     * Updates a favourite movie.
     *
     * @param values      the content values to update
     * @param whereClause the where clause
     * @param whereArgs   the where arguments
     * @return the updated row count
     * @throws SQLException if there is an error while updating the record
     * @since 1.0.2 2017/04/04
     */
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    private int updateFavourite(ContentValues values, String whereClause, String[] whereArgs) throws SQLException {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();

        try {
            return db.update(DatabaseContract.MovieEntry.TABLE_NAME, values, whereClause, whereArgs);
        } finally {
            db.close();
        }
    }
}
