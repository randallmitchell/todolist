package com.methodsignature.todolist.repository;

import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.repository.listener.DeleteItemListener;
import com.methodsignature.todolist.repository.listener.ItemsListener;
import com.methodsignature.todolist.repository.listener.ItemListener;
import com.methodsignature.todolist.repository.listener.SetItemListener;

/**
 * Created by randallmitchell on 11/1/15.
     */
public interface ItemsRepository {
    /**
     * Fetches an item by id from the repository.
     * @param itemId
     * @param listener
     */
    void requestItem(String itemId, ItemListener listener);

    /**
     * Cancels a request for an item.  Not guaranteed to prevent request from completing.
     *
     * @param itemId
     * @param listener
     */
    void cancelItemRequest(String itemId, ItemListener listener);

    /**
     * Deletes an item from the repository by id.
     * @param itemId
     * @param listener
     */
    void deleteItem(String itemId, DeleteItemListener listener);

    /**
     * Cancels a delete request for an item.  Not guaranteed to prevent deletion. Guaranteed to free
     * the listener reference from the repository.
     * @param itemId
     * @param listener
     */
    void cancelDeleteItemRequest(String itemId, DeleteItemListener listener);

    /**
     * Fetches all items in the repository
     * @param listener
     */
    void requestItems(ItemsListener listener);

    /**
     * Cancels a request for all items.  Not guaranteed to prevent request from completing.
     * Guaranteed to free the listener reference from the repository.
     * @param listener
     */
    void cancelItemsRequest(ItemsListener listener);

    /**
     * Puts an item in the repository.  If an existing repository item has the same id, it will
     * be replaced.
     * @param itemText
     * @param listener
     */
    void setItem(String itemText, SetItemListener listener);

    /**
     * Cancels a request for setting an item to the reposiotyr.  Not guaraneet to prevent request
     * from completing.  Guaranteed to free the listener reference from the repository.
     * @param item
     * @param listener
     */
    void cancelSetItemRequest(Item item, SetItemListener listener);
}
