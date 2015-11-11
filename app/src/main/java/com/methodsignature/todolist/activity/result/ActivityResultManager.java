package com.methodsignature.todolist.activity.result;

import android.content.Intent;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by randallmitchell on 11/9/15.
 */
public class ActivityResultManager {

    private final List<ActivityResultHandler> handlers = Lists.newArrayList();

    public void registerHandler(ActivityResultHandler handler) {
        handlers.add(handler);
    }

    public void unregisterHandler(ActivityResultHandler handler) {
        handlers.remove(handler);
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        for (ActivityResultHandler handler:handlers) {
            if (handler.onActivityResult(requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }
}
