package com.methodsignature.todolist.repository.listener;

import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.repository.exception.SetItemException;

/**
 * Created by randallmitchell on 11/1/15.
 */
public interface SetItemListener {
    void onSuccess(Item item);
    void onException(SetItemException exception);
}
