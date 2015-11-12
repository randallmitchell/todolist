package com.methodsignature.todolist.ui.itemlist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.methodsignature.todolist.R;
import com.methodsignature.todolist.activity.BaseActivity;
import com.methodsignature.todolist.application.TodoListApplication;
import com.methodsignature.todolist.application.ioc.ApplicationComponent;
import com.methodsignature.todolist.authentication.AuthenticationAgent;
import com.methodsignature.todolist.authentication.menu.SignOutOptionsMenuHandler;
import com.methodsignature.todolist.authentication.menu.ioc.AuthenticationMenuModule;
import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.repository.ItemsRepository;
import com.methodsignature.todolist.repository.exception.ItemsException;
import com.methodsignature.todolist.repository.exception.SetItemException;
import com.methodsignature.todolist.repository.listener.ItemsListener;
import com.methodsignature.todolist.repository.listener.SetItemListener;
import com.methodsignature.todolist.ui.itementry.NewItemDialogManager;
import com.methodsignature.todolist.ui.itementry.NewItemDialogModule;
import com.methodsignature.todolist.utility.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by randallmitchell on 11/2/15.
 */
public class ItemListActivity extends BaseActivity {

    private static final Logger LOGGER = new Logger(ItemListActivity.class);

    @Inject
    ItemsRepository itemsRepository;

    private ItemListView itemListView;
    private View signInButton;

    @Inject
    NewItemDialogManager newItemDialogManager;

    @Inject
    SignOutOptionsMenuHandler signOutOptionsMenuHandler;
    private View signInButtonContainer;

    @Inject
    AuthenticationAgent authenticationAgent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LOGGER.v("[onCreate]");
        super.onCreate(savedInstanceState);
        initializeView();
        resolveDependencies();
        initializeNewItemSupport();
        initializeSignInSupport();
    }

    private void initializeView() {
        setContentView(R.layout.item_list_activity);
        itemListView = (ItemListView) findViewById(R.id.item_list_list_view);
        signInButtonContainer = findViewById(R.id.item_list_sign_in_button_container);
        signInButton = findViewById(R.id.item_list_sign_in_button);
    }

    public void resolveDependencies() {
        ((TodoListApplication) getApplication())
                .getApplicationComponent()
                .itemListComponent(new NewItemDialogModule(), new AuthenticationMenuModule())
                .inject(this);
    }

    @Override
    protected void onStart() {
        LOGGER.v("[onStart]");
        super.onStart();
        if (authenticationAgent.shouldAutoLogin(this)) {
            authenticationAgent.authenticate(this);
        } else {
            if (authenticationAgent.isAuthenticated()) {
                setViewState(new ViewState(ViewState.LOGGED_IN));
            } else {
                setViewState(new ViewState(ViewState.LOGGED_OUT));
            }
        }
        startItemsRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        authenticationAgent.removeListener(authenticationListener);
    }

    private void handleItems(List<Item> allItems) {
        itemListView.setItems(allItems);
    }

    private void initializeNewItemSupport() {
        LOGGER.v("[initializeNewItemSupport]");
        newItemDialogManager.setListener(new NewItemDialogManager.Listener() {
            @Override
            public void onDialogResult(String newItemText) {
                handleDialogResult(newItemText);
            }

            @Override
            public void onDialogCancelled() {
                // ok to ignore
            }
        });

        View fabView = findViewById(R.id.item_list_floating_action_button);
        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItemDialogManager.showDialog(getSupportFragmentManager());
            }
        });
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

    private void handleDialogResult(String result) {
        LOGGER.v("[handleDialogResult]");
        if (TextUtils.isEmpty(result)) {
            return;
        }

        itemsRepository.setItem(result, new SetItemListener() {
            @Override
            public void onSuccess(Item item) {
                itemListView.addItem(item);
            }

            @Override
            public void onException(SetItemException exception) {
                // TODO handle repository exception
            }
        });
    }

    private void initializeSignInSupport() {
        LOGGER.v("[initializeSignInSupport]");
        authenticationAgent.addListener(authenticationListener);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationAgent.authenticate(ItemListActivity.this);
            }
        });

        signOutOptionsMenuHandler.setListener(new SignOutOptionsMenuHandler.Listener() {
            @Override
            public void onMenuItemSelected() {
                authenticationAgent.deauthenticate(ItemListActivity.this);
            }
        });
        getOptionsMenuManager().addHandler(signOutOptionsMenuHandler);
    }

    private AuthenticationAgent.Listener authenticationListener = new AuthenticationAgent.Listener() {
        @Override
        public void onAuthenticationSuccess() {
            LOGGER.v("[onSignInSuccess]");
            startItemsRequest();
            setViewState(new ViewState(ViewState.LOGGED_IN));
            Toast.makeText(ItemListActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationFailed(Exception e) {
            LOGGER.e(e);
            setViewState(new ViewState(ViewState.LOGGED_OUT));
            Toast.makeText(ItemListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDeauthencticationSuccess() {
            startItemsRequest();
            setViewState(new ViewState(ViewState.LOGGED_OUT));
            Toast.makeText(ItemListActivity.this, "Logout Successful", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDeauthenticationFailed(Exception e) {
            LOGGER.e(e);
            Toast.makeText(ItemListActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    };

    private static class ViewState {
        private final int type;
        public static final int LOGGED_IN = 0;
        public static final int LOGGED_OUT = 1;

        public ViewState(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public void setViewState(ViewState state) {
        switch (state.getType()) {
            case ViewState.LOGGED_IN:
                signInButtonContainer.setVisibility(View.GONE);
                signOutOptionsMenuHandler.setShowOptionsItem(true);
                break;
            case ViewState.LOGGED_OUT:
                signInButtonContainer.setVisibility(View.VISIBLE);
                signOutOptionsMenuHandler.setShowOptionsItem(false);
                break;
        }
        invalidateOptionsMenu();
    }
}
