package com.jfb.happyshopping.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jfb.happyshopping.R;

import org.json.JSONArray;

import java.util.Arrays;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private JSONArray jsonArray;
    String email, name, photoUrl;
    private boolean authStat;
    final String LOG_OUT = "event_logout";

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            signOut();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(LOG_OUT));

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    authStat = true;
                    email = user.getEmail();
                    name = user.getDisplayName();
                    photoUrl = user.getPhotoUrl().toString();
                    Intent i = new Intent(Login.this, MainPage.class);
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("photoUrl", photoUrl);
                    i.putExtra("Uid", user.getUid());
                    startActivity(i);
                    Log.d("Boholst", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    authStat = false;
                    Log.d("Boholst", "onAuthStateChanged:signed_out");
                }
            }
        };

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

//        loginButton.setReadPermissions("email", "public_profile", "user_location", "user_friends");
        loginButton.setOnClickListener(this);

    }

    private void handleFacebookAccessToken(final AccessToken token) {
        final String userID = token.getUserId();
        Log.d("Boholst", "handleFacebookAccessToken:" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Boholst", "signInWithCredential:onComplete:" + task.isSuccessful());
//                        ambotUnsaNi(userID, token.getCurrentAccessToken());

                        if (!task.isSuccessful()) {
                            Log.w("Boholst", "signInWithCredential", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                LoginManager.getInstance().registerCallback(mCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onError(FacebookException exception) {
                            }
                        });

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_location", "user_friends"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!authStat) {
            super.onBackPressed();
        } else {
            //do nothing
        }
    }


}



