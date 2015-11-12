package com.methodsignature.todolist.parse.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.collect.Lists;
import com.methodsignature.todolist.authentication.AuthenticationAgent;
import com.methodsignature.todolist.parse.ParseInitializer;
import com.methodsignature.todolist.utility.Logger;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Collections;
import java.util.List;

/**
 * Created by randallmitchell on 11/11/15.
 */
public class ParseTwitterAuthenticationAgent implements AuthenticationAgent {

    private static final String SHARED_PREFERENCES_NAME = ParseTwitterAuthenticationAgent.class.getName();
    private static final String SHARED_PREFERENCE_WAS_SIGNED_IN = SHARED_PREFERENCES_NAME + "_" + "WAS_SIGNED_IN";

    private static final Logger LOGGER = new Logger(ParseTwitterAuthenticationAgent.class);

    private final List<Listener> listeners = Collections.synchronizedList(Lists.<Listener>newArrayList());

    public ParseTwitterAuthenticationAgent(ParseInitializer parseInitializer) {
        parseInitializer.initializeTwitter();
    }

    @Override
    public void addListener(Listener listener) {
        LOGGER.v("[addListener]");
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        LOGGER.v("[removeListener]");
        listeners.remove(listener);
    }

    @Override
    public boolean isAuthenticated() {
        ParseUser user = ParseUser.getCurrentUser();
        return user != null && ParseTwitterUtils.isLinked(user);
    }

    @Override
    public void authenticate(final Context context) {
        LOGGER.v("[authenticate]");
        ParseTwitterUtils.logIn(context, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                LOGGER.v("[LogInCallback.done]");
                synchronized (listeners) {
                    if (user == null) {
                        for (Listener listener : listeners) {
                            listener.onAuthenticationFailed(e);
                        }
                    } else {
                        handleAuthenticatedUser(context, user);
                    }
                }
            }
        });
    }

    private void handleAuthenticatedUser(final Context context, final ParseUser user) {
        LOGGER.v("[handleAuthenticatedUser]");
        if (!ParseTwitterUtils.isLinked(user)) {
            LOGGER.v("[handleAuthenticatedUser] linking user to twitter");
            ParseTwitterUtils.link(user, context, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    LOGGER.v("[SaveCallback.done]");
                    synchronized (listeners) {
                        if (ParseTwitterUtils.isLinked(user)) {
                            setUserWasSignedIn(context, true);
                            for (Listener listener : listeners) {
                                listener.onAuthenticationSuccess();
                            }
                        } else {
                            for (Listener listener : listeners) {
                                listener.onAuthenticationFailed(e);
                            }
                        }
                    }
                }
            });
        } else {
            LOGGER.v("[handleAuthenticatedUser] user is linked");
            for (Listener listener:listeners) {
                listener.onAuthenticationSuccess();
            }
        }
    }

    @Override
    public void deauthenticate(final Context context) {
        LOGGER.v("[deauthenticate]");
        ParseUser user = ParseUser.getCurrentUser();
        if (ParseTwitterUtils.isLinked(user)) {
            ParseTwitterUtils.unlinkInBackground(user, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    LOGGER.v("[SaveCallback.done] error:" + (e == null ? e : e.getLocalizedMessage()));
                    synchronized (listeners) {
                        if (e == null) {
                            setUserWasSignedIn(context, false);
                            for (Listener listener : listeners) {
                                listener.onDeauthencticationSuccess();
                            }
                        } else {
                            for (Listener listener : listeners) {
                                listener.onDeauthenticationFailed(e);
                            }
                        }
                    }
                }
            });
        } else {
            setUserWasSignedIn(context, false);
            for (Listener listener:listeners) {
                listener.onDeauthencticationSuccess();
            }
        }
    }

    @Override
    public boolean shouldAutoLogin(Context context) {
        ParseUser user = ParseUser.getCurrentUser();
        boolean lastKnownWasLoggedIn = getSharedPreferences(context).getBoolean(SHARED_PREFERENCE_WAS_SIGNED_IN, false);
        if (!ParseTwitterUtils.isLinked(user) && lastKnownWasLoggedIn) {
            return true;
        }
        return false;
    }

    private void setUserWasSignedIn(Context context, boolean isSignedIn) {
        LOGGER.v("[setUserWasSignedIn] " + isSignedIn);
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SHARED_PREFERENCE_WAS_SIGNED_IN, isSignedIn);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
    }
}
