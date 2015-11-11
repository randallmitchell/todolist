package com.methodsignature.todolist.activity.result;

import android.content.Intent;

/**
 * Handles {@link android.app.Activity#onActivityResult(int, int, Intent)} calls.
 *
 * Created by randallmitchell on 11/9/15.
 */
public interface ActivityResultHandler {

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return true if the result was handled.
     */
    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    void setRequestCode(int requestCode);
}
