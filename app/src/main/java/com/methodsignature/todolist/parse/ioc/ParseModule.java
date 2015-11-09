package com.methodsignature.todolist.parse.ioc;

import android.content.Context;

import com.methodsignature.todolist.parse.DefaultParseInitializer;
import com.methodsignature.todolist.parse.ParseInitializer;
import com.methodsignature.todolist.repository.ItemsRepository;
import com.methodsignature.todolist.repository.ParseItemsRepository;
import com.methodsignature.todolist.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by randallmitchell on 11/8/15.
 */
@Module
public class ParseModule {

    Context applicationContext;

    public ParseModule(Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public ParseInitializer provideParseInitializer() {
        return new DefaultParseInitializer(applicationContext);
    }

    @Provides
    @ApplicationScope
    public ItemsRepository provideParseItemsRepository(ParseInitializer parseInitializer) {
        return new ParseItemsRepository(parseInitializer);
    }
}
