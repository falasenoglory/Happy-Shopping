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
import android.widget.ListView;

import com.facebook.AccessToken;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jfb.happyshopping.R;
import com.jfb.happyshopping.adapters.MemberAdapter;
import com.jfb.happyshopping.models.Friend;

import java.util.ArrayList;

public class MyContributors extends Fragment implements AdapterView.OnItemClickListener {

    DatabaseReference mRootDb;
    ArrayList<Friend> friendses = new ArrayList<>();
    ArrayList<String> friendsesKeys = new ArrayList<>();
    MemberAdapter mAdapter;
    Context mContext;
    Bundle bundle;
    String uid;

    public MyContributors() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mContext = getContext();
        mAdapter = new MemberAdapter(mContext, R.layout.list_item, new ArrayList<String>());
        listView.setAdapter(mAdapter);
        listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        listView.setOnItemClickListener(this);
        bundle = getArguments();
        uid = bundle.getString("Uid");

        mRootDb = FirebaseDatabase.getInstance().getReference();
        mRootDb.child("ShoppingList").child(AccessToken.getCurrentAccessToken().getUserId()).child("contributors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                friendses.add(new Friend(dataSnapshot.getKey().toString(), dataSnapshot.getValue().toString()));
                friendsesKeys.add(dataSnapshot.getKey().toString());
                mAdapter.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int index = friendsesKeys.indexOf(dataSnapshot.getKey().toString());
                friendsesKeys.remove(index);
                mAdapter.remove(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return view;
    }

    public void delete(String key) {
        mRootDb.child("ShoppingList").child(AccessToken.getCurrentAccessToken().getUserId()).child("contributors").child(key).removeValue();
        mRootDb.child("Users").child(key).child("contributed").child(AccessToken.getCurrentAccessToken().getUserId()).removeValue();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Configure");
        builder.setItems(new CharSequence[]
                        {"Delete"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                delete(friendses.get(position).getUserID());
                                break;
                        }
                    }
                }
        );
        builder.create().show();
    }

}
