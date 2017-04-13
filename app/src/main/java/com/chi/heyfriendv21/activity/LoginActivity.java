package com.chi.heyfriendv21.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import common.Constant;
import io.fabric.sdk.android.Fabric;
import object.User;

/**
 * Created by huuchi207 on 12/10/2016.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    LoginButton loginWithFacebookButton;
    TwitterLoginButton loginWithTwitterButton;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    public static final String TAG="LoginActivity";
    public FirebaseUser currentUser;
    private DatabaseReference mDatabaseReference;
    private Button btFacebook, btGoogle;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "1rvHQJPjgVu4dFfJ2KvWVAUpO";
    private static final String TWITTER_SECRET = "rwIOzrOYu7MRDF8jXY5o3uFTFsXtrdiYBi4AcZ05zg1upeexzr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initiate sdk
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
//        Log.e("sdfjksfFACEBOOK APP ID",FacebookSdk.getApplicationId());
        AppEventsLogger.activateApp(this);

        callbackManager= CallbackManager.Factory.create();

        //initiate database reference
        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        //check user
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        setContentView(R.layout.activity_login);

        //initiate component
        loginWithFacebookButton = (LoginButton) findViewById(R.id.realLoginFacebookButton);
        loginWithFacebookButton.setReadPermissions("email");
        loginWithTwitterButton = (TwitterLoginButton) findViewById(R.id.realLoginTwitterButton);
        btFacebook = (Button) findViewById(R.id.btFacebook);
        btGoogle = (Button) findViewById(R.id.btGoogle);
        //register onClickListener
        btFacebook.setOnClickListener(this);
        loginWithFacebookButton.setOnClickListener(this);
        btGoogle.setOnClickListener(this);
        loginWithTwitterButton.setOnClickListener(this);
        // Callback registration
        loginWithFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(LoginActivity.this, "Succeeded", Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
        loginWithTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                handleTwitterSession(session);
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginWithTwitterButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        if (v == btFacebook) {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            loginWithFacebookButton.performClick();
        }
        if (v==btGoogle){
            loginWithTwitterButton.performClick();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("facebooknewsignin", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()){
                            signIn(task);
                        }
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void signIn(Task<AuthResult> task){
        //get user from authResult
        FirebaseUser user= task.getResult().getUser();

        //separate data
        final String name= user.getDisplayName();
        final String uid= user.getUid();
        final String photoUrl= user.getPhotoUrl().toString();
        final boolean connection= !user.isAnonymous();
        final String email= user.getEmail();
        Log.e("------------------", name + "  "+ uid+ "  "+ photoUrl +"  "+ connection + "  "+ email );
        //write data to db, no need to update connected field

        mDatabaseReference.child(Constant.CHILD_USERS).addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(uid)){
                    //update state of user
//                    mDatabaseReference.child(Constant.CHILD_USERS).child(uid).child(Constant.KEY_CONNECTION).setValue(true);
                    User user= snapshot.child(uid).getValue(User.class);
                    if (user.getName()!= null && user.getUid()!= null&&
                            user.getDateOfBirth()!= null && user.getPhotoURL()!= null
                            && user.getGender()!=0){

                        Intent mainActivity= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                        finish();

                    }

                }
//                     update info
//                    registerNewLocation(uid);
//                    User userToWrite= new User(uid, email, name, photoUrl, true);
//                    Intent updateInfo = new Intent(LoginActivity.this, EditInforActivity.class);
//                    updateInfo.putExtra(Constant.USER, userToWrite);
//                    startActivity(updateInfo);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDatabaseReference != null) {
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("twitternewsignin", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()){
                            signIn(task);
                        }
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
