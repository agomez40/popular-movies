package com.example.android.popularmovies.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.data.model.Video;

import java.util.List;

/**
 *
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailersViewHolder> {
    /**
     * Class Logging tag
     */
    private static final String TAG = MovieTrailerAdapter.class.getSimpleName();
    private static final String YOUTUBE_IMG_URL = "";

    /**
     * The data collection for the adapter
     */
    private List<Video> mVideos;

    /**
     * The container context
     */
    private Context mContext;

    /**
     * @param context
     */
    public MovieTrailerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MovieTrailerAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.TrailersViewHolder holder, int position) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mVideos != null ? mVideos.size() : 0;
    }

    /**
     * @param videos The videos to set
     */
    public void setVideos(List<Video> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }

    /**
     *
     */
    class TrailersViewHolder extends RecyclerView.ViewHolder {
        public TrailersViewHolder(View itemView) {
            super(itemView);
        }
    }
}
