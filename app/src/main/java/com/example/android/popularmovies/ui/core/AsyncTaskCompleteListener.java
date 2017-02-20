package com.example.android.popularmovies.ui.core;

import android.support.annotation.StringRes;

/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 * @link http://www.jameselsey.co.uk/blogs/techblog/extracting-out-your-asynctasks-into-separate-classes-makes-your-code-cleaner/
 */
public interface AsyncTaskCompleteListener<T> {
    void onTaskComplete(T result);

    void onTaskRunning();

    void onTaskError(@StringRes int stringId);
}
