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
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.WindowManager;

import com.example.android.popularmovies.R;

/**
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 1.0.1 2017/02/19
 * @since 1.0.1 2017/02/19
 */
public final class ViewUtil {
    private static final int DEFAULT_SPAN = 2;

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

    public static RecyclerView.LayoutManager configGridLayout(Context context) {
        // Calculate the number of columns to display based on the screen size and the poster size
        int posterWidth = (int) context.getResources().getDimension(R.dimen.poster_width);

        // Calculate the optimal columns
        int columns = ViewUtil.getDisplaySize(context).x / posterWidth;

        if (columns <= 1) {
            columns = DEFAULT_SPAN;
        }

        return new GridLayoutManager(context, columns);
    }
}
