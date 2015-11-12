package com.methodsignature.todolist.application;

import android.app.Application;

import com.google.common.collect.Maps;
import com.methodsignature.todolist.application.ioc.ApplicationComponent;
import com.methodsignature.todolist.application.ioc.ComponentProvider;
import com.methodsignature.todolist.application.ioc.ComponentsProvider;
import com.methodsignature.todolist.application.ioc.DaggerApplicationComponent;
import com.methodsignature.todolist.parse.ioc.ParseModule;
import com.methodsignature.todolist.ui.itemlist.ioc.DefaultItemListComponentProvider;
import com.methodsignature.todolist.ui.itemlist.ioc.ItemListComponentProvider;

import java.util.HashMap;

/**
 * Created by randallmitchell on 11/1/15.
 */
public class TodoListApplication extends Application {

    private ApplicationComponent applicationComponent;

    private ComponentsProvider componentsProvider = ComponentsProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .parseModule(new ParseModule(getApplicationContext()))
                .build();

        registerComponents();
    }

    private void registerComponents() {
        HashMap<Class<? extends ComponentProvider>, ComponentProvider> provides = Maps.newHashMap();
        provides.put(ItemListComponentProvider.class, new DefaultItemListComponentProvider(applicationComponent));
        componentsProvider.register(provides);
    }
}
