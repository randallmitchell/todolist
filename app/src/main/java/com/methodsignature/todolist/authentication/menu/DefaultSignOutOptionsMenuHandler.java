package com.methodsignature.todolist.authentication.menu;

import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.methodsignature.todolist.R;

/**
 * Created by randallmitchell on 11/9/15.
 */
public class DefaultSignOutOptionsMenuHandler implements SignOutOptionsMenuHandler {

    private int itemId;
    private boolean show = false;
    private SignOutOptionsMenuHandler.Listener listener;

    public DefaultSignOutOptionsMenuHandler() {
    }

    public void setListener(SignOutOptionsMenuHandler.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, itemId, itemId, R.string.menu_item_sign_out);
        MenuItemCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(itemId).setVisible(show);
        return show;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == itemId) {
            listener.onMenuItemSelected();
        }
        return false;
    }

    @Override
    public void setShowOptionsItem(boolean show) {
        this.show = show;
    }
}
