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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jfb.happyshopping.R;
import com.jfb.happyshopping.adapters.MemberAdapter;
import com.jfb.happyshopping.models.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFriendsList extends Fragment implements AdapterView.OnItemClickListener {

    DatabaseReference mRootDb;
    ArrayList<Friend> friendses = new ArrayList<>();
    MemberAdapter mAdapter;
    Context mContext;
    Bundle bundle;
    String fullname;

    public MyFriendsList() {
        // Required empty public constructor
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
        fullname = bundle.getString("name");

        mRootDb = FirebaseDatabase.getInstance().getReference();
        getFriendsList();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return view;
    }

    public void add(final String id, final String email) {
        mRootDb.child("Users").orderByChild("_id").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mRootDb.child("ShoppingList").child(AccessToken.getCurrentAccessToken().getUserId()).child("contributors").child(dataSnapshot.getKey().toString()).setValue(email);
                mRootDb.child("Users").child(dataSnapshot.getKey().toString()).child("contributed").child(AccessToken.getCurrentAccessToken().getUserId()).setValue(fullname + "'s");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Configure");
        builder.setItems(new CharSequence[]
                        {"Add"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                add(friendses.get(position).getUserID(), friendses.get(position).getName());
                                break;
                        }
                    }
                }
        );
        builder.create().show();
    }


    private void getFriendsList() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/friends?fields=installed,id,name",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonArray = response.getJSONObject().getJSONArray("data");
                            for (int x = 0; x < jsonArray.length(); x++) {
                                Object object = jsonArray.get(x);
                                JSONObject jsonObject = new JSONObject(object.toString());
                                friendses.add(new Friend(jsonObject.get("id").toString(), jsonObject.get("name").toString()));
                                mAdapter.add(friendses.get(x).getName());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
