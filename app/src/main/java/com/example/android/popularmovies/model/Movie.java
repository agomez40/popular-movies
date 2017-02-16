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

/**
 * Simple POJO to represent a Movie entity
 *
 * @version 1.0.0 2016/02/12
 * @since 1.0.0 2017/02/12
 * @see Parcelable
 */
public class Movie implements Parcelable {
    /**
     * Interface that must be implemented and provided as a public CREATOR
     * field that generates instances of your Parcelable class from a Parcel
     *
     * @since 1.0.0 2017/02/12
     */
    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
    private Integer id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String posterPath;
    private Boolean adult;
    private String overview;
    private String releaseDate;
    private int[] genreIds;
    private String backdropPath;
    private Double popularity;
    private Integer voteCount;
    private Boolean video;
    private Double voteAverage;

    /**
     * Constructor
     *
     * @since 1.0.0 2017/02/12
     */
    public Movie() {
        // Default empty constructor
    }

    /**
     * Constructor
     *
     * @param in the Parcel
     * @since 1.0.0 2017/02/13
     */
    public Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        posterPath = in.readString();
        adult = (in.readInt() == 1);
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = in.createIntArray();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = (in.readInt() == 1);
        voteAverage = in.readDouble();
    }

    /**
     * Factory method, creates a new instance of {@link Movie}
     * using the provided {@link JSONObject}
     *
     * @param data the movie to create
     * @return the new instance of {@link Movie}
     * @throws JSONException if no such mapping exists
     * @since 1.0.0 2017/02/12
     */
    public static Movie newInstance(JSONObject data) throws JSONException {
        Movie movie = new Movie();
        movie.setId(data.getInt("id"));
        movie.setOriginalTitle(data.getString("original_title"));
        movie.setOriginalLanguage(data.getString("original_language"));
        movie.setTitle(data.getString("title"));
        movie.setPosterPath(data.getString("poster_path"));
        movie.setAdult(data.getBoolean("adult"));
        movie.setOverview(data.getString("overview"));
        movie.setReleaseDate(data.getString("release_date"));
        movie.setBackdropPath(data.getString("backdrop_path"));
        movie.setPopularity(data.getDouble("popularity"));
        movie.setVoteCount(data.getInt("vote_count"));
        movie.setVideo(data.getBoolean("video"));
        movie.setVoteAverage(data.getDouble("vote_average"));

        JSONArray dataGenreIds = data.getJSONArray("genre_ids");
        int[] genreIds = new int[dataGenreIds.length()];

        for (int i = 0; i < dataGenreIds.length(); i++) {
            genreIds[0] = dataGenreIds.getInt(i);
        }

        movie.setGenreIds(genreIds);

        return movie;
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
        dest.writeInt(getId());
        dest.writeString(getOriginalTitle());
        dest.writeString(getOriginalLanguage());
        dest.writeString(getTitle());
        dest.writeString(getPosterPath());
        dest.writeInt(getAdult() ? 1 : 0);
        dest.writeString(getOverview());
        dest.writeString(getReleaseDate());
        dest.writeIntArray(getGenreIds());
        dest.writeString(getBackdropPath());
        dest.writeDouble(getPopularity());
        dest.writeInt(getVoteCount());
        dest.writeInt(getVideo() ? 1 : 0);
        dest.writeDouble(getVoteAverage());
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }
}
