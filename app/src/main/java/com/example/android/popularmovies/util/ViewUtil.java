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
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.WindowManager;

import com.example.android.popularmovies.R;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.0 2017/02/09
 * @since 1.0.0 2017/02/09
 */
public final class ViewUtil {
    /**
     * Converts the given pixels to dpi units
     *
     * @param px The pixels unit
     * @return The dpi unit
     * @since 1.0.0 2017/02/18
     */
    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    /**
     * Converts the given dpi unit to pixels
     *
     * @param dp The density unit
     * @return The pixels
     * @since 1.0.0 2017/02/18
     */
    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * Gets the {@link Display} of the device
     *
     * @param context The application context
     * @return The device's {@link Display}
     * @since 1.0.0 2017/02/18
     */
    private static Point getDisplaySize(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Gets the current device orientation
     *
     * @param context The application context
     * @return the device orientation
     * @since 1.0.0 2017/02/18
     */
    private static int getDeviceOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static RecyclerView.LayoutManager configGridLayout(Context context) {
        // Calculate the number of columns to display based on the screen size and the poster size
        int posterWidth = Math.round(pxToDp(context.getResources().getDimension(R.dimen.poster_width)));

        // Calculate the optimal columns
        int columns = ViewUtil.getDisplaySize(context).x / posterWidth;

        // Check of column is even or odd, if its odd convert it to even
        if (columns % 2 != 0) {
            columns = columns - 1;
        }

        return new GridLayoutManager(context, columns);
    }
}