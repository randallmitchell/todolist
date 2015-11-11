package com.methodsignature.todolist.authentication.ioc;

import com.methodsignature.todolist.authentication.DefaultSignOutOptionsMenuHandler;
import com.methodsignature.todolist.authentication.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/10/15.
 */
@Module
public class SignOutAuthenticationModule {
    @ActivityScope
    @Provides
    public SignOutOptionsMenuHandler provideSignOutOptionsMenuHandler() {
        return new DefaultSignOutOptionsMenuHandler();
    }
}
