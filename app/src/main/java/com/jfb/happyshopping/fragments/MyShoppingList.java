package com.jfb.happyshopping.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jfb.happyshopping.R;
import com.jfb.happyshopping.adapters.ItemAdapter;
import com.jfb.happyshopping.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyShoppingList extends Fragment implements AdapterView.OnItemClickListener {

    DatabaseReference mRootDb;
    DatabaseReference mItem;
    ArrayList<String> mItemKeys = new ArrayList<>();
    ItemAdapter mAdapter;
    Context mContext;
    Bundle bundle;

    public MyShoppingList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mContext = getContext();
        mAdapter = new ItemAdapter(mContext, R.layout.list_item, new ArrayList<Product>());
        listView.setAdapter(mAdapter);
        listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        listView.setOnItemClickListener(this);
        bundle = getArguments();

        mRootDb = FirebaseDatabase.getInstance().getReference();
        mItem = mRootDb.child("ShoppingList").child(AccessToken.getCurrentAccessToken().getUserId()).child("product");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.alertdialog, null);
                dialogBuilder.setTitle("Fill in Data");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();
                        EditText edtN = (EditText) dialogView.findViewById(R.id.edtItemName);
                        EditText edtQ = (EditText) dialogView.findViewById(R.id.edtItemQuantity);
                        CheckBox cb = (CheckBox) dialogView.findViewById(R.id.cBisBought);
                        String name = edtN.getText().toString();
                        String quantity = edtQ.getText().toString();
                        Product newItem = new Product(name, Integer.parseInt(quantity), false);
                        add(newItem);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.setView(dialogView);
                b.show();
            }
        });
        mItem.addChildEventListener(mChildEventListener);
        return view;
    }

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Product it = dataSnapshot.getValue(Product.class);
            mItemKeys.add(dataSnapshot.getKey());
            mAdapter.add(it);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String Keychanged = dataSnapshot.getKey();
            int IndexChanged = mItemKeys.indexOf(Keychanged);
            Product i = dataSnapshot.getValue(Product.class);
            mAdapter.set(IndexChanged, i);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String KeyRemoved = dataSnapshot.getKey();
            int IndexRemoved = mItemKeys.indexOf(KeyRemoved);
            mItemKeys.remove(KeyRemoved);
            mAdapter.remove(IndexRemoved);

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void add(Product item) {
        String key = mItem.push().getKey();
        Map<String, Object> postValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        mItem.updateChildren(childUpdates);
    }

    public void update(int key, Product item) {
        mItem.child(mItemKeys.get(key)).setValue(item);
    }

    public void delete(int key) {
        mItem.child(mItemKeys.get(key)).removeValue();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Configure");
        builder.setItems(new CharSequence[]
                        {"Add", "Edit", "Delete"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View dialogView = inflater.inflate(R.layout.alertdialog, null);
                                dialogBuilder.setTitle("Fill in Data");
                                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //do something with edt.getText().toString();
                                        final EditText edtN = (EditText) dialogView.findViewById(R.id.edtItemName);
                                        final EditText edtQ = (EditText) dialogView.findViewById(R.id.edtItemQuantity);
                                        final CheckBox cb = (CheckBox) dialogView.findViewById(R.id.cBisBought);
                                        String name = edtN.getText().toString();
                                        String quantity = edtQ.getText().toString();
                                        Product newItem = new Product(name, Integer.parseInt(quantity), false);
                                        add(newItem);
                                    }
                                });
                                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //pass
                                    }
                                });
                                AlertDialog b = dialogBuilder.create();
                                b.setView(dialogView);
                                b.show();
                                break;
                            case 1:
                                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(mContext);
                                LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View dialogView1 = inflater1.inflate(R.layout.alertdialog, null);
                                dialogBuilder1.setTitle("Fill in Data");
                                dialogBuilder1.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //do something with edt.getText().toString();
                                        final EditText edtN = (EditText) dialogView1.findViewById(R.id.edtItemName);
                                        final EditText edtQ = (EditText) dialogView1.findViewById(R.id.edtItemQuantity);
                                        final CheckBox cb = (CheckBox) dialogView1.findViewById(R.id.cBisBought);
                                        String name = edtN.getText().toString();
                                        String quantity = edtQ.getText().toString();

                                        Product newItem = new Product(name, Integer.parseInt(quantity), true);
                                        update(position, newItem);

                                    }
                                });
                                dialogBuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //pass
                                    }
                                });
                                AlertDialog bb = dialogBuilder1.create();
                                bb.setView(dialogView1);
                                bb.show();

                                break;
                            case 2:
                                delete(position);
                                break;
                        }
                    }
                }
        );
        builder.create().show();
    }
}
