package com.methodsignature.todolist.repository.listener;

import com.methodsignature.todolist.repository.exception.DeleteItemException;

/**
 * Created by randallmitchell on 11/1/15.
 */
public interface DeleteItemListener {
    void onSuccess();
    void onException(DeleteItemException exception);
}
