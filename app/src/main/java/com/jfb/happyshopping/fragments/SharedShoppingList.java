package com.jfb.happyshopping.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jfb.happyshopping.R;
import com.jfb.happyshopping.adapters.MemberAdapter;

import java.util.ArrayList;

public class SharedShoppingList
        extends Fragment implements AdapterView.OnItemClickListener {

    public interface OnItemClick {
        void onItemClick(String id, String name);
    }

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    DatabaseReference mRootDb;
    DatabaseReference mFriendsShopping;
    ArrayList<String> mItemKeys = new ArrayList<>();
    ArrayList<String> mList = new ArrayList<>();
    MemberAdapter mAdapter;
    Context mContext;
    Bundle bundle;
    String uid;

    public SharedShoppingList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shared, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lvShared);
        mContext = getContext();
        mAdapter = new MemberAdapter(mContext, R.layout.list_item, new ArrayList<String>());
        listView.setAdapter(mAdapter);
        listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        listView.setOnItemClickListener(this);
        bundle = getArguments();
        uid = bundle.getString("Uid");

        mRootDb = FirebaseDatabase.getInstance().getReference();
        mFriendsShopping = mRootDb.child("Users").child(uid).child("contributed");
        mFriendsShopping.addChildEventListener(mChildEventListener);
        return view;
    }

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mList.add(dataSnapshot.getValue().toString());
            String email = dataSnapshot.getValue().toString();
            mItemKeys.add(dataSnapshot.getKey());
            mAdapter.add(email);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String Keychanged = dataSnapshot.getKey();
            int IndexChanged = mItemKeys.indexOf(Keychanged);
            String email = dataSnapshot.getValue().toString();
            mAdapter.set(IndexChanged, email);
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        onItemClick.onItemClick(mItemKeys.get(i), mList.get(i));
    }
}
