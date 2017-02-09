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
package com.example.android.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Basic network utils
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/09
 * @since 1.0.0 2017/02/09
 */
public class NetworkUtil {

    /**
     * Checks the device's network connection and availability
     *
     * @param context the application context
     * @return {@literal true} where there's an available network, otherwise {@literal false}
     * @since 1.0.0 2017/02/09
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Constructor
     * <p/>
     * This class can't be instantiated directly, creating an
     * instance of the class using {@code new NetworkUtil} will throw
     * an {@link AssertionError}
     * <p/>
     * All members and methods are static, and MUST be called
     * using it's static form i.e. {@code NetworkUtil.isNetworkConnected(context)}.
     *
     * @since 1.0.0 2017/02/09
     */
    private NetworkUtil() {
        /* throws an new AssertionError */
        throw new AssertionError();
    }
}
