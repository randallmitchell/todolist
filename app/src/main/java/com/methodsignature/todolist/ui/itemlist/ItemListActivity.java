package com.methodsignature.todolist.ui.itemlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.methodsignature.todolist.R;
import com.methodsignature.todolist.application.TodoListApplication;
import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.repository.ItemsRepository;
import com.methodsignature.todolist.repository.exception.ItemsException;
import com.methodsignature.todolist.repository.exception.SetItemException;
import com.methodsignature.todolist.repository.listener.ItemsListener;
import com.methodsignature.todolist.repository.listener.SetItemListener;
import com.methodsignature.todolist.ui.itementry.NewItemDialogManager;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by randallmitchell on 11/2/15.
 */
public class ItemListActivity extends AppCompatActivity {

    private ItemsRepository itemsRepository;

    private ItemListView itemListView;

    private NewItemDialogManager newItemDialogManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_list_activity);
        itemListView = (ItemListView) findViewById(R.id.item_list_list_view);

        TodoListApplication app = (TodoListApplication) getApplication();
        itemsRepository = app.getApplicationComponent().itemsRepository();

        ItemListComponent itemListComponent = DaggerItemListComponent.create();
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

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void handleItems(List<Item> allItems) {
        itemListView.setItems(allItems);
    }

    private void handleFabClick() {
        newItemDialogManager.showDialog(getSupportFragmentManager());
    }

    private void handleDialogResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return;
        }

        Item item = new Item.Builder()
                .text(result)
                .isComplete(false)
                .build();
        itemsRepository.setItem(item, new SetItemListener() {
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

    private void handleNewItemSaved(Item item) {
        itemListView.addItem(item);
    }

    private void handleDialogCancelled() {
        // ok to ignore
    }
}
