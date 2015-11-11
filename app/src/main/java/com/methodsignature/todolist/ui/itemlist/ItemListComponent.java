package com.methodsignature.todolist.ui.itemlist;

import com.methodsignature.todolist.authentication.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.authentication.ioc.SignOutAuthenticationModule;
import com.methodsignature.todolist.scopes.ActivityScope;
import com.methodsignature.todolist.ui.itementry.NewItemDialogManager;
import com.methodsignature.todolist.ui.itementry.NewItemDialogModule;

import dagger.Component;

/**
 * Created by randallmitchell on 11/4/15.
 */
@Component(
        modules = {
                NewItemDialogModule.class,
                SignOutAuthenticationModule.class
        }
)
@ActivityScope
public interface ItemListComponent {
    NewItemDialogManager newItemDialogManager();
    SignOutOptionsMenuHandler signOutOptionsMenuHandler();
}
