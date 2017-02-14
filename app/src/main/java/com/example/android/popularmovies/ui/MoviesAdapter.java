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

package com.example.android.popularmovies.ui;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/13
 * @see android.support.v7.widget.RecyclerView.Adapter
 * @see Movie
 * @since 1.0.0 2017/02/13
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    /**
     * Class tag for logging
     */
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    /**
     * The data collection for the adapter
     */
    private List<Movie> mMovies;

    /**
     * Click listener
     */
    private MovieItemClickListener mListener;

    /**
     * Constructor
     *
     * @param listener The on click listener
     * @since 1.0.0 2017/02/13
     */
    public MoviesAdapter(MovieItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     * @since 1.0.0 2017/02/13
     */
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.movie_grid_item, viewGroup, false);
        return new MoviesViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @since 1.0.0 2017/02/13
     */
    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    /**
     * Gets all the current adapter movies
     *
     * @return the items stored in the adapter
     * @since 1.0.0 2017/02/13
     */
    ArrayList<? extends Parcelable> getItems() {
        return new ArrayList<>(mMovies);
    }

    /**
     * Sets the data to display
     *
     * @param movies the collection of movies
     * @since 1.0.0 2017/02/13
     */
    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    /**
     * @since 1.0.0 2017/02/13
     */
    interface MovieItemClickListener {
        /**
         * OnMovieItemClickListener event
         *
         * @param movie the item metadata
         * @since 1.0.0 2017/02/13
         */
        void onMovieClick(Movie movie);
    }

    /**
     * A ViewHolder describes a {@link Movie }item view and metadata about its place within the RecyclerView.
     * <p>
     * <p>{@link RecyclerView.Adapter} implementations should subclass ViewHolder and add fields for caching
     * potentially expensive {@link View#findViewById(int)} results.</p>
     *
     * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
     * @version 1.0.0 2017/02/13
     * @see RecyclerView.ViewHolder
     * @since 1.0.0 2017/02/13
     */
    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * The movie poster image view
         */
        private ImageView moviePoster;

        /**
         * Constructor
         *
         * @param itemView the view to bind
         * @since 1.0.0 2017/02/13
         */
        MoviesViewHolder(View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();

            if (mListener != null) {
                mListener.onMovieClick(mMovies.get(clickedPosition));
            }
        }

        /**
         * Binds the {@link Movie} metadata to display the appropriate information within a list or
         * grid item
         *
         * @param movie the movie metadata
         * @since 1.0.0 2017/02/13
         */
        void bind(Movie movie) {
            // Use Picasso to load the image into the view
            Picasso.with(moviePoster.getContext())
                    .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                    .fit()
                    .into(moviePoster);
        }
    }
}
