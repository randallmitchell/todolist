package com.methodsignature.todolist.authentication.menu.ioc;

import com.methodsignature.todolist.authentication.menu.DefaultSignOutOptionsMenuHandler;
import com.methodsignature.todolist.authentication.menu.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/11/15.
 */
@Module
public class AuthenticationMenuModule {

    @Provides
    @ActivityScope
    public SignOutOptionsMenuHandler provideSignOutOptionsMenuHandler(){
        return new DefaultSignOutOptionsMenuHandler();
    }
}
