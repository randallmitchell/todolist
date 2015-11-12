package com.methodsignature.todolist.authentication.ioc;

import com.methodsignature.todolist.authentication.AuthenticationAgent;
import com.methodsignature.todolist.parse.twitter.ParseTwitterAuthenticationAgent;
import com.methodsignature.todolist.parse.ParseInitializer;
import com.methodsignature.todolist.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/11/15.
 */
@Module
public class AuthenticationModule {

    @Provides
    @ApplicationScope
    public AuthenticationAgent provideAuthenticationAgent(ParseInitializer parseInitializer) {
        return new ParseTwitterAuthenticationAgent(parseInitializer);
    }
}
