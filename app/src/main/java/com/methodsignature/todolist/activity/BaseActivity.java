package com.methodsignature.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.methodsignature.todolist.activity.menu.OptionsMenuManager;
import com.methodsignature.todolist.activity.result.ActivityResultManager;

/**
 * Created by randallmitchell on 11/9/15.
 */
public class BaseActivity extends AppCompatActivity {

    private ActivityResultManager activityResultManager;

    private OptionsMenuManager optionsMenuManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionsMenuManager = new OptionsMenuManager();
    }

    protected ActivityResultManager getActivityResultManager() {
        if (activityResultManager == null) {
            activityResultManager = new ActivityResultManager();
        }
        return activityResultManager;
    }

    protected  OptionsMenuManager getOptionsMenuManager() {
        return optionsMenuManager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return optionsMenuManager.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return optionsMenuManager.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return optionsMenuManager.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (activityResultManager != null && !activityResultManager.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
