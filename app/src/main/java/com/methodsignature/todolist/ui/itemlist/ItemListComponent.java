package com.methodsignature.todolist.ui.itemlist;

import android.app.Activity;

import com.methodsignature.todolist.application.ioc.ApplicationComponent;
import com.methodsignature.todolist.authentication.AuthenticationAgent;
import com.methodsignature.todolist.authentication.menu.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.authentication.menu.ioc.AuthenticationMenuModule;
import com.methodsignature.todolist.repository.ItemsRepository;
import com.methodsignature.todolist.scopes.ActivityScope;
import com.methodsignature.todolist.ui.itementry.NewItemDialogManager;
import com.methodsignature.todolist.ui.itementry.NewItemDialogModule;

import dagger.Component;

/**
 * Created by randallmitchell on 11/4/15.
 */
@Component(
        dependencies = {
                ApplicationComponent.class
        },
        modules = {
                NewItemDialogModule.class,
                AuthenticationMenuModule.class
        }

)
@ActivityScope
public interface ItemListComponent {
    NewItemDialogManager itemDialogManager();
    AuthenticationAgent authenticationAgent();
    SignOutOptionsMenuHandler signOutOptionsMenuHandler();
    ItemsRepository itemsRepository();

    void inject(ItemListActivity activity);
}
