package com.jfb.happyshopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jfb.happyshopping.Models.Product;
import com.jfb.happyshopping.adapter.ItemAdapter;
import com.jfb.happyshopping.service.MyFirebaseMessagingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DatabaseReference mItem;
    ArrayList<String> mItemKeys = new ArrayList<>();
    ItemAdapter mAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Context context;
    MyFirebaseMessagingService myFirebaseMessagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        FirebaseMessaging.getInstance().subscribeToTopic("happy-shopping");
        ListView listView = (ListView) findViewById(R.id.listView);
        context = getApplicationContext();
        mAdapter = new ItemAdapter(this, R.layout.list_item, new ArrayList<Product>());
        listView.setAdapter(mAdapter);
        listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        listView.setOnItemClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShoppingList.this);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


        DatabaseReference mRootDb = FirebaseDatabase.getInstance().getReference();
        mItem = mRootDb.child("Product");
        mItem.addChildEventListener(mChildEventListener);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        listView.setOnItemClickListener(this);
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
            Log.d("chan", String.valueOf(IndexRemoved));
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

    public void update(String key, Product item) {

        mItem.child(key).setValue(item);

    }

    public void delete(String key) {
        Log.d("chan", key);
        key = mItemKeys.get(Integer.parseInt(key));
        Log.d("chan", key);
        mItem.child(key).removeValue();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ShoppingList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jfb.happyshopping/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ShoppingList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.jfb.happyshopping/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
        Log.d("Boholst", "" + i);
        final String position = String.valueOf(i);
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingList.this);
        builder.setTitle("Configure");
        builder.setItems(new CharSequence[]
                        {"Add", "Edit", "Delete"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
//                                myFirebaseMessagingService.sendNotification("Handsome");
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShoppingList.this);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
//                                myFirebaseMessagingService.sendNotification("Pangit");
                                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(ShoppingList.this);
                                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                                        if (cb.isEnabled()) {
                                            Product newItem = new Product(name, Integer.parseInt(quantity), true);
                                            update(position, newItem);
                                        } else {
                                            Product newItem = new Product(name, Integer.parseInt(quantity), false);
                                            update(position, newItem);
                                        }

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
//                                myFirebaseMessagingService.sendNotification("Pogi");
                                break;
                        }
                    }
                }
        );
        builder.create().show();
    }
}
