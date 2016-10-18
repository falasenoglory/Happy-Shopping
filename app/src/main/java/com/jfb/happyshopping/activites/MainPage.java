package com.jfb.happyshopping.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jfb.happyshopping.fragments.MyContributors;
import com.jfb.happyshopping.fragments.MyFriendsList;
import com.jfb.happyshopping.R;
import com.jfb.happyshopping.fragments.MyShoppingList;
import com.jfb.happyshopping.fragments.SharedShoppingList;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedShoppingList.OnItemClick {

    FragmentManager fm;
    String emailAdd;
    String fullname;
    String uid;
    String tokenId;
    DatabaseReference mRootDb;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Shopping List");
        setSupportActionBar(toolbar);

        mRootDb = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Intent i = getIntent();
        TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        fullname = i.getStringExtra("name");
        name.setText(fullname);
        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        emailAdd = i.getStringExtra("email");
        email.setText(emailAdd);
        ImageView photo = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        tokenId = AccessToken.getCurrentAccessToken().getUserId();

        DatabaseReference mUsers = mRootDb.child("Users");
        uid = i.getStringExtra("Uid");
        DatabaseReference specificUser = mUsers.child(uid);
        specificUser.child("email").setValue(emailAdd);
        specificUser.child("_id").setValue(AccessToken.getCurrentAccessToken().getUserId());

        fm = getSupportFragmentManager();

        Glide.with(getApplicationContext()).load(i.getStringExtra("photoUrl")).into(photo);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_categories);
        this.onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_categories) {
            toolbar.setTitle("My Shopping List");
            fragment = new MyShoppingList();
            Bundle bundle = new Bundle();
            bundle.putString("tokenUId", tokenId);
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_shoppinglist) {
            SharedShoppingList sharedShoppingList = new SharedShoppingList();
            sharedShoppingList.setOnItemClick(this);
            fragment = sharedShoppingList;
            toolbar.setTitle("Shared To Me");
            Bundle bundle = new Bundle();
            bundle.putString("Uid", uid);
            bundle.putString("name", fullname);
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_members) {
            toolbar.setTitle("Friends");
            fragment = new MyFriendsList();
            Bundle bundle = new Bundle();
            bundle.putString("Uid", uid);
            bundle.putString("name", fullname);
            fragment.setArguments(bundle);
        } else if (id == R.id.contributors) {
            toolbar.setTitle("Contributors");
            fragment = new MyContributors();
            Bundle bundle = new Bundle();
            bundle.putString("Uid", uid);
            bundle.putString("name", fullname);
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_logout) {
            //do this on logout button click
            final String LOG_OUT = "event_logout";
            Intent intent = new Intent(LOG_OUT);
            //send the broadcast to all activities who are listening
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Intent i = new Intent(MainPage.this, Login.class);
            startActivity(i);
            fragment = new Fragment();
        }

        fm.beginTransaction()
                .replace(R.id.content_fragment, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(String id, String name) {
        toolbar.setTitle(name);
        Fragment fragment = new MyShoppingList();
        Bundle bundle = new Bundle();
        bundle.putString("tokenUId", id);
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.content_fragment, fragment).commit();
    }
}

