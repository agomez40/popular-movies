package com.example.android.popularmovies.injection.component;

import com.example.android.popularmovies.injection.context.ConfigContext;
import com.example.android.popularmovies.injection.module.ActivityModule;

import dagger.Component;

@ConfigContext
@Component(dependencies = ApplicationComponent.class)
public interface ConfigComponent {
    ActivityComponent activityComponent(ActivityModule activityModule);
}
