package com.example.android.popularmovies.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Simple entity to represent an API collection response
 *
 * @version 1.0.1 2016/02/19
 * @see Parcelable
 * @since 1.0.1 2017/02/19
 */
@AutoValue
public abstract class ReviewCollection implements Parcelable {
    public abstract Integer page();
    public abstract List<Review> results();
    public abstract Integer total_results();
    public abstract Integer total_pages();

    /**
     * Gets the AutoValue_MovieCollection.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/28
     */
    public static ReviewCollection.Builder builder() {
        return new AutoValue_ReviewCollection.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <MovieCollection>}
     * @since 1.2.0 2017/03/28
     */
    public static TypeAdapter<ReviewCollection> typeAdapter(Gson gson) {
        return new AutoValue_ReviewCollection.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection
     * </p>
     * Allows to create new instances of {@link MovieCollection}
     * using a this factory method
     *
     * @since 1.2.0 2017/03/28
     */
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract ReviewCollection.Builder setPage(Integer page);
        public abstract ReviewCollection.Builder setResults(List<Review> results);
        public abstract ReviewCollection.Builder setTotal_results(Integer total_results);
        public abstract ReviewCollection.Builder setTotal_pages(Integer total_pages);
        public abstract ReviewCollection build();
    }
}
