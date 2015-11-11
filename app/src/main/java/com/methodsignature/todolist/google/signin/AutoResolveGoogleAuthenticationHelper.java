package com.methodsignature.todolist.google.signin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.methodsignature.todolist.utility.Logger;

/**
 * Created by randallmitchell on 11/9/15.
 */
public class AutoResolveGoogleAuthenticationHelper implements GoogleAuthenticationHelper {

    private static final Logger LOGGER = new Logger(AutoResolveGoogleAuthenticationHelper.class);

    private static final String SHARED_PREFERENCES_NAME = AutoResolveGoogleAuthenticationHelper.class.getName();
    private static final String SHARED_PREFERENCE_WAS_SIGNED_IN = SHARED_PREFERENCES_NAME + "_" + "WAS_SIGNED_IN";
    GoogleSignInOptions options;
    GoogleAuthenticationHelper.Listener listener;
    GoogleApiClient client;

    GoogleSignInAccount signInAccount;

    SharedPreferences sharedPreferences;

    private int requestCode = -1;

    public AutoResolveGoogleAuthenticationHelper() {
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void startSignIn(FragmentActivity activity) {
        LOGGER.v("[startSignIn]");
        saveSharedPreferences(activity);
        if (listener == null) {
            throw new RuntimeException("You must register a listener before starting sign in process");
        }

        if (requestCode < 0) {
            throw new RuntimeException("You must set a greater than -1 request code before starting sign in process.");
        }

        GoogleApiClient client = getGoogleApiClient(activity);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.requestCode == requestCode) {
            LOGGER.v("[onActivityResult]");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                setUserWasSignedIn(true);
                signInAccount = result.getSignInAccount();
                listener.onSignInSuccess(signInAccount);
            } else {
                listener.onSignInFailed(result.getStatus());
            }
            return true;
        }
        return false;
    }

    @Override
    public Scope[] getSignInScopes() {
        return options.getScopeArray();
    }

    @Override
    public void startSignOut(FragmentActivity activity) {
        LOGGER.v("[startSignOut]");
        saveSharedPreferences(activity);
        if (listener == null) {
            throw new RuntimeException("You must register a listener before starting sign in process");
        }
        client = getGoogleApiClient(activity);
        Auth.GoogleSignInApi.signOut(client).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        signInAccount = null;
                        listener.onSignOutResult(status);
                        setUserWasSignedIn(false);
                    }
                }
        );
    }

    // FIXME  relies on the activity that is passed in when originally called.
    private GoogleApiClient getGoogleApiClient(FragmentActivity activity) {
        sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
        if (client == null) {
            LOGGER.v("[getGoogleApiClient] creating client");
            client = new GoogleApiClient.Builder(activity)
                    .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            listener.onConnectionFailure(connectionResult);
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                    .build();
            if (!client.isConnected()) {
                client.connect();
            }
        }
        return client;
    }

    @Override
    public void releaseActivity(FragmentActivity activity) {
        if (client != null) {
            client.stopAutoManage(activity);
            sharedPreferences = null;
        }
    }

    private void saveSharedPreferences(FragmentActivity activity) {
        sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
    }

    private void setUserWasSignedIn(boolean isSignedIn) {
        LOGGER.v("[setUserWasSignedIn] " + isSignedIn);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCE_WAS_SIGNED_IN, isSignedIn);
        editor.commit();
    }

    @Override
    public boolean getUserWasSignedIn(FragmentActivity activity) {
        saveSharedPreferences(activity);
        return sharedPreferences.getBoolean(SHARED_PREFERENCE_WAS_SIGNED_IN, false);
    }

    @Override
    public void tryToAutoSignIn(FragmentActivity activity) {
        LOGGER.v("[tryToAutoSignIn]");
        saveSharedPreferences(activity);
        if (getUserWasSignedIn(activity) && client == null) {
            startSignIn(activity);
        }
    }
}
