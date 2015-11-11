package com.methodsignature.todolist.google.signin;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.methodsignature.todolist.activity.result.ActivityResultHandler;

/**
 * Helps facilitate authentication via Google Play services.
 * Created by randallmitchell on 11/9/15.
 */
public interface GoogleAuthenticationHelper extends ActivityResultHandler {

    interface Listener {
        void onConnectionFailure(ConnectionResult result);
        void onSignInFailed(Status status);
        void onSignInSuccess(GoogleSignInAccount account);
        void onSignOutResult(Status status);
    }

    void setListener(Listener listener);

    void startSignIn(FragmentActivity activity);

    /**
     * These scopes are used by the Google {@link com.google.android.gms.common.SignInButton} to
     * help determine how it should look.
     * @return
     */
    Scope[] getSignInScopes();

    void startSignOut(FragmentActivity activity);

    /**
     * Ensure that the helper doesn't leak the activity to the Google SDK.
     * @param activity
     */
    void releaseActivity(FragmentActivity activity);

    /**
     * will attempt to sign the user in if they were previously signed in.
     */
    void tryToAutoSignIn(FragmentActivity activity);

    /**
     * See if the last time we knew, the user was signed in.
     * @param activity
     * @return
     */
    public boolean getUserWasSignedIn(FragmentActivity activity);
}
