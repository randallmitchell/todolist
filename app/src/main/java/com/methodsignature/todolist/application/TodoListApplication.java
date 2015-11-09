package com.methodsignature.todolist.application;

import android.app.Application;

import com.methodsignature.todolist.application.ioc.ApplicationComponent;
import com.methodsignature.todolist.application.ioc.DaggerApplicationComponent;
import com.methodsignature.todolist.parse.ioc.ParseModule;

/**
 * Created by randallmitchell on 11/1/15.
 */
public class TodoListApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .parseModule(new ParseModule(getApplicationContext()))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
