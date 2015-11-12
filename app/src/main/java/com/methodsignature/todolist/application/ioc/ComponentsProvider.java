package com.methodsignature.todolist.application.ioc;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * Created by randallmitchell on 11/12/15.
 */
public class ComponentsProvider {

    private static ComponentsProvider componentsProvider;

    private ComponentsProvider() {

    }

    public static ComponentsProvider getInstance() {
        if (componentsProvider == null) {
            componentsProvider = new ComponentsProvider();
        }
        return componentsProvider;
    }

    private final HashMap<Class<? extends ComponentProvider>, ComponentProvider> providers =
            Maps.newHashMap();

    public ComponentProvider provide(Class<? extends ComponentProvider> type) {
        ComponentProvider provider = providers.get(type);
        if (provider == null) {
            throw new RuntimeException("Component provider not registered.");
        }
        return provider;
    }

    public void register(HashMap<Class<? extends ComponentProvider>, ComponentProvider> providers) {
        this.providers.putAll(providers);
    }
}
