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

package com.example.android.popularmovies.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.model.Review;
import com.example.android.popularmovies.data.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.2.0 2017/03/30
 * @see android.support.v7.widget.RecyclerView.Adapter
 * @see Video
 * @since 1.2.0 2017/03/30
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailersViewHolder> {
    /**
     * Class Logging tag
     */
    private static final String TAG = MovieTrailerAdapter.class.getSimpleName();

    /**
     * Youtube Data API URL
     */
    private static final String YOUTUBE_IMG_URL = "https://i.ytimg.com/vi/{id}/hqdefault.jpg";

    /**
     * The data collection for the adapter
     */
    private List<Video> mVideos;

    /**
     * The container context
     */
    private Context mContext;

    /**
     * RecyclerView click listener
     */
    private ItemClickListener mListener;

    /**
     * Constructor
     *
     * @param context  The application context
     * @param listener The RecyclerView click listener
     * @since 1.2.0 2017/03/30
     */
    public MovieTrailerAdapter(Context context, ItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        Timber.d(TAG.concat(" Created."));
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
     * @since 1.2.0 2017/03/30
     */
    @Override
    public MovieTrailerAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.trailer_item, viewGroup, false);

        return new TrailersViewHolder(view);
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
     * @since 1.2.0 2017/03/30
     */
    @Override
    public void onBindViewHolder(MovieTrailerAdapter.TrailersViewHolder holder, int position) {
        Video video = mVideos.get(position);
        holder.tvTitle.setText(video.name());

        // Load the image using picasso
        String url = YOUTUBE_IMG_URL.replace("{id}", video.key());
        Picasso.with(mContext).load(url)
                .placeholder(R.drawable.vector_movie_placeholder)
                .into(holder.ivThumbnail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mVideos != null ? mVideos.size() : 0;
    }

    /**
     * Sets the video collection to display
     *
     * @param videos The videos to set
     * @since 1.2.0 2017/03/30
     */
    public void setVideos(List<Video> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }

    /**
     * @since 1.2.0 2017/03/30
     */
    public interface ItemClickListener {
        /**
         * OnItemClickListener event
         *
         * @param video the item metadata
         * @since 1.2.0 2017/03/30
         */
        void onVideoClick(Video video);
    }

    /**
     * A ViewHolder describes a {@link Review }item view and metadata about its place within the RecyclerView.
     * <p>
     * <p>{@link RecyclerView.Adapter} implementations should subclass ViewHolder and add fields for caching
     * potentially expensive {@link View#findViewById(int)} results.</p>
     *
     * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
     * @version 1.2.0 2017/03/30
     * @see RecyclerView.ViewHolder
     * @since 1.2.0 2017/03/30
     */
    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final ImageView ivThumbnail;

        /**
         * Constructor
         *
         * @param view the view to set
         * @since 1.2.0 2017/03/30
         */
        TrailersViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tv_video_title);
            ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);

            ivThumbnail.setOnClickListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            if (mListener != null) {
                mListener.onVideoClick(mVideos.get(clickedPosition));
            }
        }
    }
}
