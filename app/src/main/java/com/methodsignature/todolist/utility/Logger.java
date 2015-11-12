package com.methodsignature.todolist.utility;

import android.util.Log;

/**
 * Created by randallmitchell on 9/8/15.
 */
public class Logger {

    private final String tag;
    private boolean disabled = false;

    public Logger(String tag) {
        this(tag, false);
    }

    public Logger(String tag, boolean disabled) {
        this.tag = tag;
        this.disabled = disabled;
    }

    public Logger(Class clazz) {
        this(clazz, false);
    }

    public Logger(Class clazz, boolean disabled) {
        this(clazz.getSimpleName(), disabled);
    }

    public void e(String message) {
        if (!disabled) {
            Log.e(tag, message);
        }
    }

    public void v(String message) {
        if (!disabled) {
            Log.v(tag, message);
        }
    }

    public void w(String message) {
        if (!disabled) {
            Log.w(tag, message);
        }
    }

    public void e(Throwable throwable) {
        if (!disabled) {
            Log.e(tag, throwable.getMessage(),throwable);
        }
    }

    public void w(String message, Throwable throwable) {
        if (!disabled) {
            Log.w(tag, message, throwable);
        }
    }
}
