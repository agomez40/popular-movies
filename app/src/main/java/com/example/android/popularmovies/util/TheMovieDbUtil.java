package com.example.android.popularmovies.util;

public class TheMovieDbUtil {
    /**
     * https://developers.themoviedb.org/3/movies/get-top-rated-movies
     */
    public static final String GET_TOP_RATED_MOVIES = "top_rated";
    /**
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     */
    public static final String GET_POPULAR_MOVIES = "popular";

    /**
     * The movie database base API URI
     */
    public static final String MOVIE_DATABASE_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    /**
     * Sort by most popular
     */
    public final static short SORT_MOST_POPULAR = 1;

    /**
     * Sort by top rated
     */
    public final static short SORT_TOP_RATED = 2;

    /**
     * Results default page
     */
    public static final int DEFAULT_PAGE = 1;
}
