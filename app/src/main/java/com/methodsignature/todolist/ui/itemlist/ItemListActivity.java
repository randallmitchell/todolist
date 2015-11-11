package com.methodsignature.todolist.ui.itemlist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Status;
import com.google.common.collect.ImmutableList;
import com.methodsignature.todolist.R;
import com.methodsignature.todolist.activity.BaseActivity;
import com.methodsignature.todolist.application.TodoListApplication;
import com.methodsignature.todolist.authentication.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.google.signin.GoogleAuthenticationHelper;
import com.methodsignature.todolist.google.signin.ioc.DaggerGoogleSignInComponent;
import com.methodsignature.todolist.google.signin.ioc.GoogleSignInComponent;
import com.methodsignature.todolist.google.signin.ioc.GoogleSignInModule;
import com.methodsignature.todolist.repository.ItemsRepository;
import com.methodsignature.todolist.repository.exception.ItemsException;
import com.methodsignature.todolist.repository.exception.SetItemException;
import com.methodsignature.todolist.repository.listener.ItemsListener;
import com.methodsignature.todolist.repository.listener.SetItemListener;
import com.methodsignature.todolist.ui.itementry.NewItemDialogManager;
import com.methodsignature.todolist.utility.Logger;

import java.util.List;

/**
 * Created by randallmitchell on 11/2/15.
 */
// FIXME this activity has memory leak.
// CONCERN consider abstracting play services further out of the UI layer.
public class ItemListActivity extends BaseActivity {

    private static final Logger LOGGER = new Logger(ItemListActivity.class);

    private static final int ACTIVITY_RESULT_CODE_SIGN_IN = 0;

    ItemListComponent itemListComponent;

    private ItemsRepository itemsRepository;

    private ItemListView itemListView;

    private NewItemDialogManager newItemDialogManager;

    private GoogleAuthenticationHelper googleAuthenticationHelper;

    private View googleSignInButtonContainer;
    private SignOutOptionsMenuHandler signOutOptionsMenuHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LOGGER.v("[onCreate]");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_list_activity);
        itemListView = (ItemListView) findViewById(R.id.item_list_list_view);
        googleSignInButtonContainer = findViewById(R.id.item_list_google_sign_in_button_container);

        TodoListApplication app = (TodoListApplication) getApplication();
        itemsRepository = app.getApplicationComponent().itemsRepository();

        itemListComponent = DaggerItemListComponent.create();
        initializeNewItemSupport(itemListComponent);

        GoogleSignInComponent daggerGoogleSignInComponent = DaggerGoogleSignInComponent.builder()
                .googleSignInModule(new GoogleSignInModule())
                .build();
        initializeGoogleSignInSupport(daggerGoogleSignInComponent);
    }

    @Override
    protected void onStart() {
        LOGGER.v("[onStart]");
        super.onStart();
        startItemsRequest();
        if (googleAuthenticationHelper.getUserWasSignedIn(this)) {
            LOGGER.v("[getUserWasSignedIn] attempting auto sign in.");
            googleAuthenticationHelper.tryToAutoSignIn(this);
        } else {
            LOGGER.v("[getUserWasSignedIn] displaying login button.");
            setViewState(new ViewState(ViewState.STATE_LOGGED_OUT));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleAuthenticationHelper.releaseActivity(this);
    }

    private void handleItems(List<Item> allItems) {
        itemListView.setItems(allItems);
    }

    private void initializeNewItemSupport(ItemListComponent itemListComponent) {
        LOGGER.v("[initializeNewItemSupport]");
        newItemDialogManager = itemListComponent.newItemDialogManager();
        newItemDialogManager.setListener(new NewItemDialogManager.Listener() {
            @Override
            public void onDialogResult(String newItemText) {
                handleDialogResult(newItemText);
            }

            @Override
            public void onDialogCancelled() {
                handleDialogCancelled();
            }
        });

        View fabView = findViewById(R.id.item_list_floating_action_button);
        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFabClick();
            }
        });

    }

    private void handleNewItemSaved(Item item) {
        itemListView.addItem(item);
    }

    private void startItemsRequest() {
        LOGGER.v("[startItemsRequest]");
        itemsRepository.requestItems(new ItemsListener() {
            @Override
            public void onAllItems(ImmutableList<Item> allItems) {
                handleItems(allItems);
            }

            @Override
            public void onException(ItemsException exception) {
                // TODO handle items error
            }
        });
    }

    private void handleFabClick() {
        newItemDialogManager.showDialog(getSupportFragmentManager());
    }

    private void handleDialogResult(String result) {
        LOGGER.v("[handleDialogResult]");
        if (TextUtils.isEmpty(result)) {
            return;
        }

        itemsRepository.setItem(result, new SetItemListener() {
            @Override
            public void onSuccess(Item item) {
                handleNewItemSaved(item);
            }

            @Override
            public void onException(SetItemException exception) {
                // TODO handle repository exception
            }
        });
    }

    private void handleDialogCancelled() {
        // ok to ignore
    }

    private void initializeGoogleSignInSupport(GoogleSignInComponent googleSignInComponent) {
        LOGGER.v("[initializeGoogleSignInSupport]");
        googleAuthenticationHelper = googleSignInComponent.googleSignInHelper();
        signOutOptionsMenuHandler = itemListComponent.signOutOptionsMenuHandler();

        googleAuthenticationHelper.setListener(googleSignInHelperListener);
        googleAuthenticationHelper.setRequestCode(ACTIVITY_RESULT_CODE_SIGN_IN);
        getActivityResultManager().registerHandler(googleAuthenticationHelper);

        SignInButton signInButton = (SignInButton) findViewById(R.id.item_list_google_sign_in_button);
        signInButton.setScopes(googleAuthenticationHelper.getSignInScopes());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME this should be pushed to a background thread.
                googleAuthenticationHelper.startSignIn(ItemListActivity.this);
            }
        });

        signOutOptionsMenuHandler.setListener(signOutMenuHandlerListener);
        getOptionsMenuManager().addHandler(signOutOptionsMenuHandler);
    }

    private SignOutOptionsMenuHandler.Listener signOutMenuHandlerListener = new SignOutOptionsMenuHandler.Listener() {
        @Override
        public void onMenuItemSelected() {
            googleAuthenticationHelper.startSignOut(ItemListActivity.this);
        }
    };

    private GoogleAuthenticationHelper.Listener googleSignInHelperListener = new GoogleAuthenticationHelper.Listener() {
        @Override
        public void onConnectionFailure(ConnectionResult result) {
            LOGGER.v("[onConnectionFailure]");
            // TODO handle connection failure
            Toast.makeText(ItemListActivity.this, "Failed to connect to Google.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSignInFailed(Status status) {
            LOGGER.v("[onSignInFailed]");
            // TODO handle sign in failure
            Toast.makeText(ItemListActivity.this, "Failed to sign in to Google.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSignInSuccess(GoogleSignInAccount account) {
            LOGGER.v("[onSignInSuccess]");
            setViewState(new ViewState(ViewState.STATE_LOGGED_IN));
        }

        @Override
        public void onSignOutResult(Status status) {
            LOGGER.v("[onSignOutResult]");
            setViewState(new ViewState(ViewState.STATE_LOGGED_OUT));
        }
    };

    private static class ViewState {
        public final int state;
        public static final int STATE_LOGGED_IN = 0;
        public static final int STATE_LOGGED_OUT = 1;

        public ViewState(int state) {
            this.state = state;
        }
    }

    public void setViewState(ViewState state) {
        switch (state.state) {
            case ViewState.STATE_LOGGED_IN:
                googleSignInButtonContainer.setVisibility(View.GONE);
                signOutOptionsMenuHandler.setShowOptionsItem(true);
                break;
            case ViewState.STATE_LOGGED_OUT:
                googleSignInButtonContainer.setVisibility(View.VISIBLE);
                signOutOptionsMenuHandler.setShowOptionsItem(false);
                break;
        }
        invalidateOptionsMenu();
    }
}
