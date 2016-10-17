package com.jfb.happyshopping.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jfb.happyshopping.R;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mLayoutId;
    private List<String> mItems = new ArrayList<>();

    public MemberAdapter(Context context, int resource, List<String> names) {
        super(context, resource, names);

        mContext = context;
        mLayoutId = resource;
        mItems = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MemberAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            viewHolder = new MemberAdapter.ViewHolder();
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
            viewHolder.itemQuantity = (TextView) convertView.findViewById(R.id.txtItemQuantity);
            viewHolder.isBought = (CheckBox) convertView.findViewById(R.id.cBisBought);
            viewHolder.itemQuantity.setVisibility(View.GONE);
            viewHolder.isBought.setVisibility(View.GONE);
            viewHolder.isBought.setFocusable(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MemberAdapter.ViewHolder) convertView.getTag();
        }
        String names = mItems.get(position);
        if (names != null) {
            if (viewHolder.itemName != null) {
                viewHolder.itemName.setText(names);
            }
        }
        return convertView;
    }


    public void remove(int key) {
        mItems.remove(key);
        notifyDataSetChanged();
    }

    public void add(String name) {
        mItems.add(name);
        notifyDataSetChanged();
    }

    public void set(int changedIndex, String name) {
        if (changedIndex == 0 && changedIndex < mItems.size() && name != null) {
            mItems.set(changedIndex, name);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        public TextView itemName;
        public TextView itemQuantity;
        public CheckBox isBought;
    }
}

