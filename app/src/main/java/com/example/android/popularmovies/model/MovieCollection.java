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
package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple POJO to represent an API Movie collection response
 *
 * @version 1.0.1 2016/02/19
 * @see Parcelable
 * @since 1.0.1 2017/02/19
 */
public class MovieCollection implements Parcelable {

    /**
     * Interface that must be implemented and provided as a public CREATOR
     * field that generates instances of your Parcelable class from a Parcel
     *
     * @since 1.0.0 2017/02/12
     */
    public static final Creator<MovieCollection> CREATOR = new Creator<MovieCollection>() {
        @Override
        public MovieCollection createFromParcel(Parcel in) {
            return new MovieCollection(in);
        }

        @Override
        public MovieCollection[] newArray(int size) {
            return new MovieCollection[size];
        }
    };
    private Integer mPage;
    private List<Movie> mMovies;
    private Integer mTotalMovies;
    private Integer mTotalPages;

    /**
     * Constructor
     */
    public MovieCollection() {
        // Default empty constructor
    }

    /**
     * Constructor
     *
     * @param in the Parcel
     * @since 1.0.0 2017/02/13
     */
    protected MovieCollection(Parcel in) {
        mPage = in.readInt();
        in.readList(mMovies, Movie.class.getClassLoader());
        mTotalMovies = in.readInt();
        mTotalPages = in.readInt();
    }

    /**
     * Factory method, creates a new instance of {@link MovieCollection}
     * using the provided {@link JSONObject}
     *
     * @param data the movie to create
     * @return the new instance of {@link MovieCollection}
     * @throws JSONException if no such mapping exists
     * @since 1.0.1 2017/02/19
     */
    public static MovieCollection newInstance(JSONObject data) throws JSONException {
        MovieCollection movieCollection = new MovieCollection();
        movieCollection.setPage(data.getInt("page"));
        movieCollection.setTotalMovies(data.getInt("total_results"));
        movieCollection.setTotalPages(data.getInt("total_pages"));

        // The movies!
        JSONArray results = data.getJSONArray("results");

        ArrayList<Movie> movies = new ArrayList<>(0);
        for (int i = 0; i < results.length(); i++) {
            movies.add(Movie.newInstance(results.getJSONObject(i)));
        }

        movieCollection.setMovies(movies);

        return movieCollection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getPage());
        dest.writeTypedList(getMovies());
        dest.writeInt(getTotalMovies());
        dest.writeInt(getTotalPages());
    }

    public Integer getPage() {
        return mPage;
    }

    public void setPage(Integer page) {
        this.mPage = page;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
    }

    public Integer getTotalMovies() {
        return mTotalMovies;
    }

    public void setTotalMovies(Integer totalMovies) {
        this.mTotalMovies = totalMovies;
    }

    public Integer getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.mTotalPages = totalPages;
    }
}
