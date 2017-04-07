package com.example.android.popularmovies.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * REview entity, as defined by:
 * <a href="https://developers.themoviedb.org/3/movies/get-movie-reviews">TheMovieDB.org API</a>
 * <p>
 * Optional fields are annotated by @Nullable, so Gson can ignore them when using the
 * TypeAdapterFactory.
 *
 * @author Luis Alberto Gómez Rodrígurez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/18
 * @see Parcelable
 * @since 1.2.0 2017/03/18
 */
@AutoValue
public abstract class Review implements Parcelable {
    public abstract String id();
    public abstract String author();
    public abstract String content();
    public abstract String url();

    /**
     * Gets the AutoValue_Review.Builder()
     *
     * @return the class Builder
     * @since 1.2.0 2017/03/18
     */
    public static Movie.Builder builder() {
        return new AutoValue_Movie.Builder();
    }

    /**
     * Type adapter used by Gson and Dagger2 AutoValue
     *
     * @param gson the gson instance
     * @return {@link TypeAdapter <Card>}
     * @since 5.0.0 2016/12/14
     */
    public static TypeAdapter<Review> typeAdapter(Gson gson) {
        return new AutoValue_Review.GsonTypeAdapter(gson);
    }

    /**
     * Class Builder dagger 2 injection.
     * </p>
     * Allows to create new instances of {@link Movie}
     * using this factory method.
     *
     * @since 5.0.0 2016/12/14
     */
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Review.Builder setId(String id);
        public abstract Review.Builder setAuthor(String author);
        public abstract Review.Builder setContent(String content);
        public abstract Review.Builder setUrl(String url);
        public abstract Review build();
    }
}
