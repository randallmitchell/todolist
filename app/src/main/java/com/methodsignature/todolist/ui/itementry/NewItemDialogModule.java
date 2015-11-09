package com.methodsignature.todolist.ui.itementry;

import com.methodsignature.todolist.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/4/15.
 */
@Module
public class NewItemDialogModule {

    @Provides
    @ActivityScope
    public NewItemDialogManager provideNewItemDialogManager() {
        return new DefaultNewItemDialogManager();
    }
}
