package com.methodsignature.todolist.parse;

import android.content.Context;

import com.methodsignature.todolist.R;
import com.methodsignature.todolist.parse.data.ParseItem;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by randallmitchell on 11/8/15.
 */
public class DefaultParseInitializer implements ParseInitializer {

    private Context applicationContext;

    public DefaultParseInitializer(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        // allows us to save data locally
        Parse.enableLocalDatastore(applicationContext.getApplicationContext());

        // allows us to register
        ParseObject.registerSubclass(ParseItem.class);

        Parse.initialize(
                applicationContext,
                applicationContext.getString(R.string.parse_application_id),
                applicationContext.getString(R.string.parse_client_key));

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
