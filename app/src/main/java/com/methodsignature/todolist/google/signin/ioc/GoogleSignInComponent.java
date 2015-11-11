package com.methodsignature.todolist.google.signin.ioc;

import com.methodsignature.todolist.authentication.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.google.signin.GoogleAuthenticationHelper;
import com.methodsignature.todolist.scopes.ActivityScope;

import dagger.Component;

/**
 * Created by randallmitchell on 11/9/15.
 */
@Component (
        modules = GoogleSignInModule.class
)
@ActivityScope
public interface GoogleSignInComponent {
    GoogleAuthenticationHelper googleSignInHelper();
}
