package com.methodsignature.todolist.ui.itemlist.ioc;

import com.methodsignature.todolist.application.ioc.ComponentProvider;

/**
 * Created by randallmitchell on 11/12/15.
 */
public interface ItemListComponentProvider extends ComponentProvider {
    ItemListComponent provideItemListComponent();
}
