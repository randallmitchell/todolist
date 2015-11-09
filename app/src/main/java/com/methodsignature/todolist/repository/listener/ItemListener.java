package com.methodsignature.todolist.repository.listener;

import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.repository.exception.ItemException;

/**
 * Created by randallmitchell on 11/1/15.
 */
public interface ItemListener {
    void onItem(Item item);
    void onException(ItemException exception);
}
