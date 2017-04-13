package service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import common.CommonMethod;
import common.Constant;

/**
 * Created by root on 21/10/2016.
 */

public class OnStopAppService extends Service {
    DatabaseReference mDatabaseReference;
    FirebaseUser currentFirebaseUser;
    FirebaseAuth mAuth;
    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();
        return START_STICKY;
    }

    @Override
    public final IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
//        Toast.makeText(getApplicationContext(), "APP KILLED", Toast.LENGTH_LONG).show(); // here your app is killed by user

        try {
            if (CommonMethod.getCurrentFirebaseUser()!= null){
                mDatabaseReference.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_CONNECTION).setValue(false);
                mDatabaseReference.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("ds-------", dataSnapshot.getValue().toString());
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
//                            Log.e("dssad-------", ds.getKey().toString());
                            if (ds.hasChild(currentFirebaseUser.getUid())){
//                                Log.e("dssad-------", ds.child(currentFirebaseUser.getUid()).getValue().toString());
                                final DataSnapshot finalDs = ds;
                                mDatabaseReference.getRoot().child(Constant.CHILD_FRIENDLISTS).
                                        child(ds.getKey()).child(currentFirebaseUser.getUid()).
                                        child(Constant.KEY_LASTONLINE).
                                        setValue(CommonMethod.getCurrentTime(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError!= null){
//                                            Log.e("completion error", databaseError.getDetails());
                                        }
                                        else{
                                                mDatabaseReference.getRoot().child(Constant.CHILD_FRIENDLISTS).
                                                    child(finalDs.getKey()).child(currentFirebaseUser.getUid()).
                                                    child(Constant.KEY_CONNECTION).setValue(false, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if(databaseError!= null){
                                                        Log.e("completion error", databaseError.getDetails());
                                                    }
                                                    else{
                                                        stopService(new Intent(OnStopAppService.this, OnStopAppService.class));
                                                        stopService(new Intent(OnStopAppService.this, ReceiveMessageService.class));
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).addListenerForSingleValueEvent(valueEventListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            super.onTaskRemoved(rootIntent);
        } else{}
    }
}
