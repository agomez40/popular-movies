package com.example.android.popularmovies.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmovies.PopularMoviesApp;
import com.example.android.popularmovies.injection.component.ActivityComponent;
import com.example.android.popularmovies.injection.component.ConfigComponent;
import com.example.android.popularmovies.injection.component.DaggerConfigComponent;
import com.example.android.popularmovies.injection.module.ActivityModule;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 *
 * @author Luis Alberto Gómez Rodríguez (alberto.gomez@cargomovil.com)
 * @version 1.2.0 2017/03/18
 * @see AppCompatActivity
 * @see ActivityComponent
 * @since 1.2.0 2017/03/18
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * The activity component
     */
    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent
        ConfigComponent configComponent = DaggerConfigComponent.builder()
                .applicationComponent(PopularMoviesApp.getApplication(this).getComponent())
                .build();
        mActivityComponent = configComponent.activityComponent(new ActivityModule(this));
    }

    /**
     * Gets the application's singleton activity component
     *
     * @return the singleton {@link ActivityComponent}
     * @since 1.2.0 2017/03/18
     */
    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }
}
