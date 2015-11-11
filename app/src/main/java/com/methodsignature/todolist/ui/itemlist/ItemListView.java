package com.methodsignature.todolist.ui.itemlist;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.methodsignature.todolist.R;
import com.methodsignature.todolist.data.Item;
import com.methodsignature.todolist.utility.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by randallmitchell on 11/2/15.
 */
public class ItemListView extends RecyclerView {

    private static final Logger LOGGER = new Logger(ItemListView.class);

    private final List<Item> items = Lists.newArrayList();

    private SimpleDateFormat dateFormat;

    public ItemListView(Context context) {
        super(context);
        init();
    }

    public ItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        setLayoutManager(layout);
        addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.vertical_gully_small)));
        setAdapter(adapter);

        String dateFormatString = getContext().getResources().getString(R.string.item_list_date_format);
        dateFormat = new SimpleDateFormat(dateFormatString);
    }

    public void setItems(List<Item> items) {
        LOGGER.v("[setItems] size:" + items.size());
        this.items.clear();
        for (int i=0; i< items.size(); i++) {
            this.items.add(items.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    public void addItem(Item item) {
        LOGGER.v("[addItem] text:" + item.getText());
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    private static class ItemViewHolder extends ViewHolder {
        public final View root;
        public final CheckBox checkBox;
        public final TextView text;
        public final TextView date;

        public ItemViewHolder(View view) {
            super(view);
            root = view;
            checkBox = (CheckBox) view.findViewById(R.id.item_list_item_checkbox);
            text = (TextView) view.findViewById(R.id.item_list_item_heading);
            date = (TextView) view.findViewById(R.id.item_list_item_subheading);
        }
    }

    private Adapter<ItemViewHolder> adapter = new Adapter<ItemViewHolder>() {
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_list_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            if (items != null) {
                Item item = items.get(position);
                holder.checkBox.setChecked(item.isComplete());
                holder.text.setText(item.getText());
                Date date = new Date(item.getTimestamp());
                holder.date.setText(dateFormat.format(date));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    };

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildLayoutPosition(view) == 0)
                outRect.top = space;
        }
    }
}
