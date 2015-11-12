package com.methodsignature.todolist.parse;

import android.content.Context;

import com.methodsignature.todolist.R;
import com.methodsignature.todolist.parse.data.ParseItem;
import com.methodsignature.todolist.utility.Logger;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

/**
 * Created by randallmitchell on 11/8/15.
 */
public class DefaultParseInitializer implements ParseInitializer {

    private static final Logger LOGGER = new Logger(DefaultParseInitializer.class);

    private Context applicationContext;
    private boolean initialized = false;
    private boolean twitterInitialized = false;
    public DefaultParseInitializer(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize() {
        if (!initialized) {
            LOGGER.v("[initialize] initializing");
            initialized = true;
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
        } else {
            LOGGER.v("[initialize] already initialized");
        }
    }

    @Override
    public void initializeTwitter() {
        if (!twitterInitialized) {
            LOGGER.v("[initializeTwitter] initializing twitter");
            if (!initialized) {
                initialize();
            }
            ParseTwitterUtils.initialize(applicationContext.getString(R.string.twitter_customer_key), applicationContext.getString(R.string.twitter_secret_key));
        } else {
            LOGGER.v("[initializeTwitter] already initialized");
        }
    }
}
