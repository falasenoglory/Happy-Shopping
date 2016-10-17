package com.jfb.happyshopping.adapters;

/**
 * Created by Christian on 9/7/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jfb.happyshopping.models.Product;

import java.util.ArrayList;
import java.util.List;

import com.jfb.happyshopping.R;

/**
 * Created by Christian on 3/4/2016.
 */
public class ItemAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private int    mLayoutId;
    private List<Product> mItems= new ArrayList<>();

    public ItemAdapter(Context context, int resource, List<Product> shopl) {
        super(context, resource, shopl);

        mContext = context;
        mLayoutId = resource;
        mItems = shopl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate the layout
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);

            // create the view holder
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
            viewHolder.itemQuantity = (TextView) convertView.findViewById(R.id.txtItemQuantity);
            viewHolder.isBought = (CheckBox) convertView.findViewById(R.id.cBisBought);
            viewHolder.isBought.setFocusable(false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the movie data
        Product it = mItems.get(position);

        if (it != null) {
            if (viewHolder.itemName != null) {
                viewHolder.itemName.setText(it.getItemName());
            }
            if (viewHolder.itemQuantity != null) {
                viewHolder.itemQuantity.setText(String.valueOf(it.getQuantity()));
            }
            if (viewHolder.isBought != null) {
                if(viewHolder.isBought.isEnabled()) {
                    viewHolder.isBought.setChecked(true);
                }else{
                    viewHolder.isBought.setChecked(false);

                }
                }
        }

        return convertView;
    }
    public void remove(int key){
        mItems.remove(key);
      notifyDataSetChanged();
    }
    public void add(Product item){
        mItems.add(item);
        notifyDataSetChanged();
    }
    public void set(int changedIndex, Product it){
        if(changedIndex == 0 && changedIndex <mItems.size() && it!=null){
            mItems.set(changedIndex,it);
            notifyDataSetChanged();
        }
    }
    private static class ViewHolder {
        public TextView  itemName;
        public TextView  itemQuantity;
        public CheckBox  isBought;
    }

}
