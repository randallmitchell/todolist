package com.methodsignature.todolist.application.ioc;

import com.methodsignature.todolist.authentication.AuthenticationAgent;
import com.methodsignature.todolist.authentication.ioc.AuthenticationModule;
import com.methodsignature.todolist.authentication.menu.ioc.AuthenticationMenuModule;
import com.methodsignature.todolist.parse.ioc.ParseModule;
import com.methodsignature.todolist.repository.ItemsRepository;
import com.methodsignature.todolist.scopes.ApplicationScope;
import com.methodsignature.todolist.ui.itementry.NewItemDialogModule;
import com.methodsignature.todolist.ui.itemlist.ItemListComponent;

import dagger.Component;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/2/15.
 */
@Component(
        modules = {
                ParseModule.class,
                AuthenticationModule.class
        }
)
@ApplicationScope
public interface ApplicationComponent {
    ItemsRepository itemsRepository();
    AuthenticationAgent authenticationAgent();
    ItemListComponent itemListComponent(
        NewItemDialogModule itemDialogModule,
        AuthenticationMenuModule authenticationMenuModule);
}
