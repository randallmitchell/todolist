package com.methodsignature.todolist.authentication;

import android.content.Context;

/**
 * Created by randallmitchell on 11/11/15.
 */
public interface AuthenticationAgent {
    public interface Listener {
        void onAuthenticationSuccess();
        void onAuthenticationFailed(Exception e);
        void onDeauthencticationSuccess();
        void onDeauthenticationFailed(Exception e);
    }

    void addListener(Listener listener);
    void removeListener(Listener listener);

    boolean isAuthenticated();

    void authenticate(Context context);
    void deauthenticate(Context context);

    /**
     * See if the last time we knew, the user was signed in.  Good for checking at application launch.
     * @param context
     * @return
     */
    boolean shouldAutoLogin(Context context);
}
