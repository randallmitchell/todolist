package com.methodsignature.todolist.repository.exception;

/**
 * Created by randallmitchell on 11/1/15.
 */
public class DeleteItemException extends Exception {

    public static final int TYPE_ITEM_NOT_FOUND = 0;
    public static final int TYPE_UNKNOWN = 1;

    private int type;

    public DeleteItemException(int exceptionType, String message) {
        super(message);
        type = exceptionType;
    }

    public int getType() {
        return type;
    }
}
