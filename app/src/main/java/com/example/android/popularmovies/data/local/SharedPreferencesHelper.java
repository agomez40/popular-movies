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

package com.example.android.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.injection.context.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A SharedPreferences Helper, allows CRUD operations on the application
 * SharedPreferences file.
 *
 * @author Luis Alberto Gómez Rodríguez (lagomez40@gmail.com)
 * @version 5.0.0 2016/11/10
 * @see Singleton
 * @see PreferenceManager
 * @see SharedPreferences
 * @since 5.0.0 2016/11/10
 */
@Singleton
public class SharedPreferencesHelper {
    /**
     * The SharedPreferences
     */
    private final SharedPreferences mSharedPreferences;

    /**
     * Constructor
     * </p>
     * Injects the application context using dagger 2
     *
     * @param context the application context
     * @since 5.0.0 2016/11/10
     */
    @Inject
    SharedPreferencesHelper(@ApplicationContext Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Gets the specified shared preferences entry
     * </p>
     * The only supported type class for entries are:
     * <ul>
     * <li>String</li>
     * <li>Integer</li>
     * <li>Long</li>
     * <li>Float</li>
     * <li>Boolean</li>
     * </ul>
     *
     * @param entryKey   the shared preferences entry key
     * @param entryClass the value type to get
     * @return the value for the given entry
     * @throws UnsupportedOperationException when the provided param entryClass is a valid type
     * @since 1.2.0 2017/03/18
     */
    public Object getEntry(String entryKey, Class<?> entryClass) throws UnsupportedOperationException {
        if (entryClass.equals(Integer.class)) {
            return mSharedPreferences.getInt(entryKey, -1);
        } else if (entryClass.equals(Boolean.class)) {
            return mSharedPreferences.getBoolean(entryKey, false);
        } else if (entryClass.equals(Float.class)) {
            return mSharedPreferences.getFloat(entryKey, -1);
        } else if (entryClass.equals(Long.class)) {
            return mSharedPreferences.getLong(entryKey, -1L);
        } else if (entryClass.equals(String.class)) {
            return mSharedPreferences.getString(entryKey, "");
        } else
            throw new UnsupportedOperationException("The class is not supported by the SharedPreferences.");
    }

    /**
     * Sets the specified shared preferences entry
     * </p>
     * The only supported type class for entries are:
     * <ul>
     * <li>String</li>
     * <li>Integer</li>
     * <li>Long</li>
     * <li>Float</li>
     * <li>Boolean</li>
     * </ul>
     *
     * @param entryKey the shared preferences entry key
     * @param value    the value to set
     * @throws UnsupportedOperationException when the provided param entryClass is a valid type
     * @since 1.2.0 2017/03/18
     */
    public void setEntry(String entryKey, Object value) throws UnsupportedOperationException {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();

        try {
            if (value instanceof Integer) {
                prefs.putInt(entryKey, (Integer) value);
            } else if (value instanceof Boolean) {
                prefs.putBoolean(entryKey, (Boolean) value);
            } else if (value instanceof Float) {
                prefs.putFloat(entryKey, (Float) value);
            } else if (value instanceof Long) {
                prefs.putLong(entryKey, (Long) value);
            } else if (value instanceof String) {
                prefs.putString(entryKey, (String) value);
            } else
                throw new UnsupportedOperationException("The class is not supported by the SharedPreferences.");
        } finally {
            prefs.apply();
        }
    }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns {@literal true} if the preference exists in the preferences,
     * otherwise {@literal false}.
     * @since 5.0.0 2017/03/02
     */
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * Removes the shared preferences entry on the set
     *
     * @param key the entry to delete
     * @since 1.2.0 2017/03/18
     */
    public void remove(String key) {
        SharedPreferences.Editor prefs = mSharedPreferences.edit();
        prefs.remove(key).apply();
    }

    /**
     * Clears the SharedPreferences file
     *
     * @since 1.2.0 2017/03/18
     */
    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
