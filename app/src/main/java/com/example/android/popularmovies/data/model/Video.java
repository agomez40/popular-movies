package com.example.android.popularmovies.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Simple entity to represent an API Video response
 *
 * @version 1.0.1 2016/02/19
 * @see Parcelable
 * @since 1.0.1 2017/02/19
 */
@AutoValue
public abstract class Video implements Parcelable {
    public abstract String id();

    public abstract String iso_639_1();

    public abstract String iso_3166_1();

    public abstract String key();

    public abstract String name();

    public abstract String site();

    public abstract Integer size();

    public abstract String type();

    /**
     * Gets the AutoValue_Video.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/18
     */
    public static Video.Builder builder() {
        return new AutoValue_Video.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <Card>}
     * @since 5.0.0 2016/12/14
     */
    public static TypeAdapter<Video> typeAdapter(Gson gson) {
        return new AutoValue_Video.GsonTypeAdapter(gson);
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
        public abstract Video.Builder setId(String id);

        public abstract Video.Builder setIso_639_1(String iso_639_1);

        public abstract Video.Builder setIso_3166_1(String iso_3166_1);

        public abstract Video.Builder setKey(String key);

        public abstract Video.Builder setName(String name);

        public abstract Video.Builder setSite(String site);

        public abstract Video.Builder setSize(Integer size);

        public abstract Video.Builder setType(String type);

        public abstract Video build();
    }
}
