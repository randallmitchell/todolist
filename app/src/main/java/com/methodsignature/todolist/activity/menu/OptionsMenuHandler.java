package com.methodsignature.todolist.activity.menu;

import android.view.Menu;
import android.view.MenuItem;

/**
 * Handles interaction with options menu items.
 *
 * Created by randallmitchell on 11/9/15.
 */
public interface OptionsMenuHandler {
    void setItemId(int id);
    boolean onCreateOptionsMenu(Menu menu);
    boolean onPrepareOptionsMenu(Menu menu);
    boolean onOptionsItemSelected(MenuItem item);
}
