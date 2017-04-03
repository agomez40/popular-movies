package com.example.android.popularmovies.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.model.Review;

import java.util.List;

import timber.log.Timber;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/13
 * @see android.support.v7.widget.RecyclerView.Adapter
 * @see Review
 * @since 1.2.0 2017/03/30
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    /**
     * Class Logging tag
     */
    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    /**
     * The data collection for the adapter
     */
    private List<Review> mReviews;

    /**
     * The container context
     */
    private Context mContext;

    /**
     * Constructor
     *
     * @param context The application context
     * @since 1.2.0 2017/03/30
     */
    public ReviewsAdapter(Context context) {
        this.mContext = context;
        Timber.d(TAG, "Created.");
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
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.review_item, viewGroup, false);

        return new ReviewsViewHolder(view);
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
    public void onBindViewHolder(ReviewsAdapter.ReviewsViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.setItem(review);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mReviews != null ? mReviews.size() : 0;
    }

    /**
     * Sets the data collection to display
     *
     * @param reviews The movie reviews collection
     * @since 1.2.0 2017/03/30
     */
    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
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
    class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView tvDetail;
        private Review review;

        /**
         * Constructor
         *
         * @param itemView The item view to set
         * @since 1.2.0 2017/03/30
         */
        ReviewsViewHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            tvDetail = (TextView) itemView.findViewById(R.id.tv_view_more);
            tvDetail.setOnClickListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v) {
            // TODO open the url on the browser using an intent
            review.url();
        }

        /**
         * Sets the data on the UI widgets
         *
         * @param item The current review
         * @since 1.2.0 2017/03/30
         */
        void setItem(Review item) {
            review = item;
            tvAuthor.setText(item.author());
            tvContent.setText(item.content().trim());
        }
    }
}
