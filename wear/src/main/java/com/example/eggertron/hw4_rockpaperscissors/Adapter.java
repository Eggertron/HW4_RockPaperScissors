package com.example.eggertron.hw4_rockpaperscissors;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by eggertron on 11/2/16.
 */
public class Adapter extends WearableListView.Adapter {

    // create LayoutInflater to inflate the UI
    private final LayoutInflater mInflater;
    private String[] mDataset;

    public Adapter(Context context, String[] dataset) {
        Context mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
    }

    // Create a static class to refer to the WearableListItemLayout we just created.
    public static class ItemViewHolder extends WearableListView.ViewHolder {

        private TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    // Now we can use the ItemViewHolder to finish the derived methods.
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;
        view.setText(mDataset[position]);
        ((ItemViewHolder) holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        //return 0;
        return mDataset.length;
    }

    // Now go back to MainActivity in the Wear module to finish it.
}
