package com.methodsignature.todolist.ui.itemlist.ioc;

import com.methodsignature.todolist.application.ioc.ApplicationComponent;
import com.methodsignature.todolist.ui.itementry.NewItemDialogModule;

/**
 * Created by randallmitchell on 11/12/15.
 */
public class DefaultItemListComponentProvider implements ItemListComponentProvider {

    private ApplicationComponent applicationComponent;

    public DefaultItemListComponentProvider(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    @Override
    public ItemListComponent provideItemListComponent() {
        return DaggerItemListComponent.builder()
                .applicationComponent(applicationComponent)
                .newItemDialogModule(new NewItemDialogModule())
                .build();
    }
}
