package com.example.android.popularmovies.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailersViewHolder>{
    @Override
    public MovieTrailerAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.TrailersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder {
        public TrailersViewHolder(View itemView) {
            super(itemView);
        }
    }
}
