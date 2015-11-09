package com.methodsignature.todolist.repository.exception;

/**
 * Created by randallmitchell on 11/1/15.
 */
public class ItemException extends Exception {

    public static final int TYPE_ITEM_NOT_FOUND = 0;

    private int type;
    public ItemException(int type, String message) {
        super(message);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
