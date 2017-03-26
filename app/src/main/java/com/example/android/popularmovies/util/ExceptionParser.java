package com.example.android.popularmovies.util;

import android.support.annotation.StringRes;

import com.example.android.popularmovies.R;

/**
 *
 */
public class ExceptionParser {

    /**
     *
     * @param exception
     * @return
     */
    public static @StringRes int parseException(Exception exception) {
        // SocketTimeoutException
        // retrofit2.adapter.rxjava2.HttpException: HTTP 401 Unauthorized
        return R.string.error_no_network;
    }
}
