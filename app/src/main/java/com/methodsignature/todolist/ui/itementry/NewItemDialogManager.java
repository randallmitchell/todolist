package com.methodsignature.todolist.ui.itementry;


import android.support.v4.app.FragmentManager;

/**
 * Created by randallmitchell on 11/4/15.
 */
public interface NewItemDialogManager {

    public interface Listener {
        void onDialogResult(String newItemText);
        void onDialogCancelled();
    }
    void setListener(Listener listener);
    void showDialog(FragmentManager fragmentManager);
    void cancelDialog();
}
