package com.methodsignature.todolist.data;

/**
 * Created by randallmitchell on 11/1/15.
 */
public class Item {

    private String id;
    private long timestamp;
    private boolean isComplete;
    private String text;

    private Item() {

    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getText() {
        return text;
    }

    public static class Builder {
        private Item item;

        public Builder() {
            item = new Item();
        }

        public Builder id(String id) {
            item.id = id;
            return this;
        }

        public Builder timestamp(long timestamp) {
            item.timestamp = timestamp;
            return this;
        }

        public Builder isComplete(boolean isComplete) {
            item.isComplete = isComplete;
            return this;
        }

        public Builder text(String text) {
            item.text = text;
            return this;
        }

        public Item build() {
            return item;
        }
    }
}
