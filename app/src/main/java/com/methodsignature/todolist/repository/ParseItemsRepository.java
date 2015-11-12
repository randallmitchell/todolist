package com.methodsignature.todolist.repository;

import android.os.Handler;
import android.text.TextUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.parse.ParseInitializer;
import com.methodsignature.todolist.parse.data.ParseItem;
import com.methodsignature.todolist.repository.exception.DeleteItemException;
import com.methodsignature.todolist.repository.exception.ItemException;
import com.methodsignature.todolist.repository.exception.ItemsException;
import com.methodsignature.todolist.repository.listener.DeleteItemListener;
import com.methodsignature.todolist.repository.listener.ItemListener;
import com.methodsignature.todolist.repository.listener.ItemsListener;
import com.methodsignature.todolist.repository.listener.SetItemListener;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by randallmitchell on 11/1/15.
 */
// FIXME reliance on runnable null state is functionally incorrect.
public class ParseItemsRepository implements ItemsRepository {


    private final List<Item> items = Collections.synchronizedList(new ArrayList<Item>());

    private final Handler handler = new Handler();

    private final Object itemRequestLock = new Object();
    private final Multimap<String, ItemListener> itemListeners = Multimaps.synchronizedMultimap(ArrayListMultimap.<String, ItemListener>create());

    private final Object itemsRequestLock = new Object();
    private final List<ItemsListener> itemsListeners = Collections.synchronizedList(Lists.<ItemsListener>newArrayList());

    private final Object deleteItemRequestLock = new Object();
    private final Multimap<String, DeleteItemListener> deleteItemListeners = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, DeleteItemListener>create());

    private final Object setItemRequestLock = new Object();
    private final Multimap<String, SetItemListener> setItemListeners = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, SetItemListener>create());

    public ParseItemsRepository(ParseInitializer parseInitializer) {
        parseInitializer.initialize();
    }

    @Override
    public void requestItem(String itemId, ItemListener listener) {
        synchronized (itemRequestLock) {
            itemListeners.put(itemId, listener);
            handler.post(new RequestItemRunnable(itemId));
        }
    }

    @Override
    public void cancelItemRequest(String itemId, ItemListener listener) {
        synchronized (itemRequestLock) {
            itemListeners.remove(itemId, listener);
        }
    }

    private class RequestItemRunnable implements Runnable {

        private String itemId;

        public RequestItemRunnable(String itemId) {
            this.itemId = itemId;
        }

        @Override
        public void run() {

            ParseQuery<ParseItem> query = ParseQuery.getQuery(ParseItem.class.getSimpleName());
            query.whereContains(ParseItem.DATA_ID, itemId);
            query.findInBackground(new FindCallback<ParseItem>() {
                @Override
                public void done(List<ParseItem> list, ParseException e) {
                    synchronized (itemRequestLock) {
                        if (list.size() < 0) {
                            onItemException(new ItemException(ItemException.TYPE_ITEM_NOT_FOUND, "Item was not found in repository."));
                        } else {
                            onItemFetched(list.get(0).toItem());
                        }
                    }
                }
            });
        }

        private void onItemFetched(Item item) {
            Collection<ItemListener> listeners = itemListeners.removeAll(itemId);
            for (ItemListener listener:listeners) {
                listener.onItem(item);
            }
        }

        private void onItemException(ItemException exception) {
            Collection<ItemListener> listeners = itemListeners.removeAll(itemId);
            for (ItemListener listener:listeners) {
                listener.onException(exception);
            }
        }
    }

    @Override
    public void requestItems(ItemsListener listener) {
        synchronized (itemsRequestLock) {
            itemsListeners.add(listener);
            handler.post(new RequestItemsRunnable());
        }
    }

    private class RequestItemsRunnable implements Runnable {

        @Override
        public void run() {
            ParseQuery<ParseItem> query = ParseQuery.getQuery(ParseItem.class.getSimpleName());
            query.findInBackground(new FindCallback<ParseItem>() {
                @Override
                public void done(List<ParseItem> list, ParseException e) {
                    if (list != null) {
                        ImmutableList.Builder<Item> builder = new ImmutableList.Builder<Item>();
                        for (ParseItem item:list) {
                            builder.add(item.toItem());
                        }
                        handleItems(builder.build());
                    } else {
                        handleItemsException(e);
                    }
                }
            });

        }
    }

    private void handleItems(ImmutableList<Item> items) {
        synchronized (itemsRequestLock) {
            this.items.addAll(items);
            for (int i=0; i<itemsListeners.size(); i++) {
                itemsListeners.get(i).onAllItems(items);
            }
            itemsListeners.clear();
        }
    }

    private void handleItemsException(ParseException e) {
        synchronized (itemsRequestLock) {
            this.items.addAll(items);
            for (int i=0; i<itemsListeners.size(); i++) {
                itemsListeners.get(i).onException(new ItemsException(e.getMessage()));
            }
            itemsListeners.clear();
        }
    }

    @Override
    public void cancelItemsRequest(ItemsListener listener) {
        synchronized (itemsRequestLock) {
            itemsListeners.remove(listener);
        }
    }

    @Override
    public void deleteItem(String itemId, DeleteItemListener listener) {
        synchronized (deleteItemRequestLock) {
            deleteItemListeners.put(itemId, listener);
            handler.post(new DeleteItemRunnable(itemId));
        }
    }

    @Override
    public void cancelDeleteItemRequest(String itemId, DeleteItemListener listener) {
        synchronized (deleteItemRequestLock) {
            itemListeners.remove(itemId, listener);
        }
    }

    private class DeleteItemRunnable implements Runnable {

        private String itemId;

        public DeleteItemRunnable(String itemId) {
            this.itemId = itemId;
        }

        @Override
        public void run() {
            ParseItem deleteItem = new ParseItem();
            deleteItem.setId(itemId);
            deleteItem.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        onItemDeleted();
                    } else {
                        onException(new DeleteItemException(DeleteItemException.TYPE_UNKNOWN, "An unkown error occured while trying to delete the item."));
                    }
                }
            });
        }

        private void onItemDeleted() {
            synchronized (deleteItemRequestLock) {
                Collection<DeleteItemListener> listeners = deleteItemListeners.removeAll(itemId);
                for (DeleteItemListener listener:listeners) {
                    listener.onSuccess();
                }
            }
        }

        private void onException(DeleteItemException exception) {
            synchronized (deleteItemRequestLock) {
                Collection<DeleteItemListener> listeners = deleteItemListeners.removeAll(itemId);
                for (DeleteItemListener listener:listeners) {
                    listener.onException(exception);
                }
            }
        }
    }

    @Override
    public void setItem(String itemText, SetItemListener listener) {
        Item item = new Item.Builder()
                .text(itemText)
                .isComplete(false)
                .build();

        synchronized (setItemRequestLock) {
            Item itemToSave;
            if (TextUtils.isEmpty(item.getId())) {
                itemToSave = convertToItemWithId(item, Long.toString(System.currentTimeMillis()));
            } else {
                itemToSave = item;
            }
            setItemListeners.put(itemToSave.getId(), listener);
            handler.post(new SetItemRunnable(itemToSave));
        }
    }

    private Item convertToItemWithId(Item item, String itemId) {
        return new Item.Builder()
                .id(itemId)
                .timestamp(System.currentTimeMillis())
                .isComplete(item.isComplete())
                .text(item.getText())
                .build();

    }

    private class SetItemRunnable implements Runnable {

        private Item item;
        private ParseItem parseItem;

        public SetItemRunnable(Item item) {
            this.item = item;
        }

        @Override
        public void run() {
            parseItem = new ParseItem();
            parseItem.setTimestamp(this.item.getTimestamp());
            parseItem.setIsComplete(this.item.isComplete());
            parseItem.setText(this.item.getText());
            parseItem.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    onItemSaved();
                }
            });
        }

        private void onItemSaved() {
            synchronized (itemsRequestLock) {
                String listenerKey = item.getId();
                if (!TextUtils.equals(item.getId(), parseItem.getObjectId())) {
                    parseItem.setId(parseItem.getObjectId());
                    item = convertToItemWithId(item, parseItem.getObjectId());
                    parseItem.saveEventually();
                }
                Collection<SetItemListener> listeners = setItemListeners.removeAll(listenerKey);
                for (SetItemListener listener:listeners) {
                    listener.onSuccess(item);
                }
            }
        }
    }

    @Override
    public void cancelSetItemRequest(Item item, SetItemListener listener) {
        synchronized (setItemRequestLock) {
            setItemListeners.remove(item.getId(), listener);
        }
    }
}
