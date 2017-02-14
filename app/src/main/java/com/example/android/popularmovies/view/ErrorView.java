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

package com.example.android.popularmovies.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.R;

/**
 * A custom view that displays an error image, and an error message.
 *
 * @version 1.0.0 2017/02/10
 * @since 1.0.0 2017/02/10
 */
public class ErrorView extends LinearLayout {

    /**
     * The error message
     */
    private TextView mErrorMessage;

    /**
     * A button to retry
     */
    private Button mRetryButton;

    /**
     * The listener for the button callback interface
     */
    private ErrorViewListener mErrorViewListener;

    /**
     * Constructor
     *
     * @param context the application context
     * @since 1.0.0 2017/02/10
     */
    public ErrorView(Context context) {
        super(context);
        inflate(context);
    }

    /**
     * Constructor
     *
     * @param context the application context
     * @param attrs   the view attributes
     * @since 1.0.0 2017/02/10
     */
    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    /**
     * Constructor
     *
     * @param context      the application context
     * @param attrs        the view attributes
     * @param defStyleAttr the default style
     * @since 1.0.0 2017/02/10
     */
    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context);
    }

    /**
     * Inflates the custom view widgets
     *
     * @param context the application context
     * @since 1.0.0 2017/02/10
     */
    private void inflate(Context context) {
        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_error_view, this, true);
        setGravity(Gravity.CENTER);

        // Allow for animations
        setLayoutTransition(new LayoutTransition());

        // Get the widgets instances
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mRetryButton = (Button) findViewById(R.id.bt_retry);

        // By default the retry button is gone, it will turn visible when a ErrorViewListener is set
        mRetryButton.setVisibility(View.GONE);

        // Event binding method
        bind();
    }

    /**
     * Binds the view events
     *
     * @since 1.0.0 2017/02/10
     */
    private void bind() {
        // Click Listener on Retry button
        mRetryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if a listener is attached
                if (mErrorViewListener != null) {
                    mErrorViewListener.onRetryClick();
                } else {
                    Log.e(ErrorView.class.getSimpleName(), "Can't retry, attach a callback ErrorViewListener!.");
                }
            }
        });
    }

    /**
     * Sets the TextView error message to display
     *
     * @param errorMessage the message to display
     * @since 1.0.0 2017/02/10
     */
    public void setErrorMessage(String errorMessage) {
        mErrorMessage.setText(errorMessage);
    }

    /**
     * Shows or hides the retry button
     *
     * @param show {@literal true} to show the Retry button, or {@literal false}
     *             to hide the button
     * @since 1.0.0 2017/02/10
     */
    private void showRetry(boolean show) {
        if (show) {
            mRetryButton.setVisibility(View.VISIBLE);
        } else {
            mRetryButton.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the {@link ErrorViewListener} callback to handle
     * the retry button click event.
     *
     * @param listener the listener to attach
     * @since 1.0.0 2017/02/10
     */
    public void setErrorViewListener(ErrorViewListener listener) {
        mErrorViewListener = listener;
        showRetry(true);
    }

    /**
     * Callback interface to communicate with the parent activity or fragment
     *
     * @since 1.0.0 2017/02/10
     */
    public interface ErrorViewListener {
        /**
         * Button click listener
         *
         * @since 1.0.0 2017/02/10
         */
        void onRetryClick();
    }
}
