package com.methodsignature.todolist.authentication;

import com.methodsignature.todolist.activity.menu.OptionsMenuHandler;

/**
 * Created by randallmitchell on 11/10/15.
 */
public interface SignOutOptionsMenuHandler extends OptionsMenuHandler {
    interface Listener {
        void onMenuItemSelected();
    }

    void setListener(Listener listener);

    void setShowOptionsItem(boolean show);
}
