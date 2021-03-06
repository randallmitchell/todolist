package com.methodsignature.todolist.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by randallmitchell on 11/4/15.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
