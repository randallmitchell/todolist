package com.methodsignature.todolist.repository.listener;

import com.google.common.collect.ImmutableList;
import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.repository.exception.ItemsException;

/**
 * Created by randallmitchell on 11/1/15.
 */
public interface ItemsListener {
    void onAllItems(ImmutableList<Item> allItems);
    void onException(ItemsException exception);
}
