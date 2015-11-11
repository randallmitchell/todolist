package com.methodsignature.todolist.google.signin.ioc;

import com.methodsignature.todolist.authentication.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.google.signin.AutoResolveGoogleAuthenticationHelper;
import com.methodsignature.todolist.google.signin.GoogleAuthenticationHelper;
import com.methodsignature.todolist.authentication.DefaultSignOutOptionsMenuHandler;
import com.methodsignature.todolist.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/9/15.
 */
@Module
public class GoogleSignInModule {
    @ActivityScope
    @Provides
    public GoogleAuthenticationHelper provideGoogleSignInHelper() {
        return new AutoResolveGoogleAuthenticationHelper();
    }
}
