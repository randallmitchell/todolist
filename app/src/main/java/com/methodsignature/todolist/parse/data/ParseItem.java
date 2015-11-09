package com.methodsignature.todolist.parse.data;

import com.methodsignature.todolist.data.Item;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by randallmitchell on 11/8/15.
 */
@ParseClassName("ParseItem")
public class ParseItem extends ParseObject {

    public static final String DATA_ID = "id";
    public static final String DATA_TIMESTAMP = "timestamp";
    public static final String DATA_IS_COMPLETE = "is_complete";
    public static final String DATA_TEXT = "text";

    public String getId() {
        return getString(DATA_ID);
    }

    public void setId(String id) {
        put(DATA_ID, id);
    }

    public long getTimestamp() {
        return getLong(DATA_TIMESTAMP);
    }

    public void setTimestamp(long timestamp) {
        put(DATA_TIMESTAMP, timestamp);
    }

    public boolean isComplete() {
        return getBoolean(DATA_IS_COMPLETE);
    }

    public void setIsComplete(boolean isComplete) {
        put(DATA_IS_COMPLETE, isComplete);
    }

    public String getText() {
        return getString(DATA_TEXT);
    }

    public void setText(String text) {
        put(DATA_TEXT, text);
    }

    public Item toItem() {
        return new Item.Builder()
                .id(getId())
                .timestamp(getTimestamp())
                .isComplete(isComplete())
                .text(getText())
                .build();
    }
}
