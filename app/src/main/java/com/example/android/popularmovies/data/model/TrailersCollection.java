package com.example.android.popularmovies.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Simple entity to represent an API Trailers collection response
 *
 * @version 1.0.1 2016/02/19
 * @see Parcelable
 * @since 1.0.1 2017/02/19
 */
@AutoValue
public abstract class TrailersCollection {
    public abstract Integer id();

    public abstract List<Video> results();

    /**
     * Gets the AutoValue_TrailersCollection.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/18
     */
    public static TrailersCollection.Builder builder() {
        return new AutoValue_TrailersCollection.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <Card>}
     * @since 5.0.0 2016/12/14
     */
    public static TypeAdapter<TrailersCollection> typeAdapter(Gson gson) {
        return new AutoValue_TrailersCollection.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection.
     * </p>
     * Allows to create new instances of {@link Video}
     * using this factory method.
     *
     * @since 5.0.0 2016/12/14
     */
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract TrailersCollection.Builder setId(Integer id);

        public abstract TrailersCollection.Builder setResults(List<Video> results);

        public abstract TrailersCollection build();
    }
}
