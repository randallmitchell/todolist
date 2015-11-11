package com.methodsignature.todolist.activity.menu;

import android.view.Menu;
import android.view.MenuItem;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by randallmitchell on 11/9/15.
 */
public class OptionsMenuManager {

    private int itemIdCount = 0;

    private final List<OptionsMenuHandler> handlers = Lists.newArrayList();

    public void addHandler(OptionsMenuHandler handler) {
        handlers.add(handler);
        handler.setItemId(itemIdCount++);
    }

    public void removeHandler(OptionsMenuHandler handler) {
        handlers.remove(handler);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean displayMenu = false;
        boolean result;
        for (OptionsMenuHandler handler:handlers) {
            result = handler.onCreateOptionsMenu(menu);
            if (result) {
                displayMenu = result;
            }
        }
        return displayMenu;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean displayMenu = false;
        boolean result;
        for (OptionsMenuHandler handler:handlers) {
            result = handler.onPrepareOptionsMenu(menu);
            if (result) {
                displayMenu = true;
            }
        }
        return displayMenu;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        for (OptionsMenuHandler handler:handlers) {
            if (handler.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }
}
