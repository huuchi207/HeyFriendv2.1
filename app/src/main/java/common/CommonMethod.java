package common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import com.chi.heyfriendv21.R;

public class CommonMethod {
    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }
    public static String convertLongToTime(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        Date inputDate= new Date(time);
        String outputDate= formatter.format(inputDate);
        return outputDate;
    }
    public static String convertLongToDate(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date inputDate= new Date(time);
        String outputDate= formatter.format(inputDate);
        return outputDate;
    }
    public static String diffTime(Context context, long time){
        long diffTime = (getCurrentTime()- time)/1000;

            if (diffTime < 60) {     // rule 1
                return String.format(context.getString(R.string.txt_about)+ " %s "+ context.getString(R.string.txt_seconds_ago), diffTime);
            } else if (diffTime < 3600) {  // rule 2
                return String.format(context.getString(R.string.txt_about)+ " %s "+ context.getString(R.string.txt_minutes_ago), diffTime/60);
            }else if (diffTime < 86400)
                return String.format(context.getString(R.string.txt_about)+" %s "+ context.getString(R.string.txt_hours_ago), diffTime/3600);
            else if (diffTime < 432000)
                return String.format(context.getString(R.string.txt_about)+ " %s "+ context.getString(R.string.txt_days_ago), diffTime/86400);
        return convertLongToDate(time);
    }
    public static String diffTimeForListFriend(Context context, long time){
        long diffTime = (getCurrentTime()- time)/1000;

        if (diffTime < 60) {     // rule 1
            return String.format( "%s "+ context.getString(R.string.txt_seconds_ago), diffTime);
        } else if (diffTime < 3600) {  // rule 2
            return String.format("%s "+ context.getString(R.string.txt_minutes_ago), diffTime/60);
        }else if (diffTime < 86400)
            return String.format("%s "+ context.getString(R.string.txt_hours_ago), diffTime/3600);
        else if (diffTime < 432000)
            return String.format("%s "+ context.getString(R.string.txt_days_ago), diffTime/86400);
        return convertLongToDate(time);
    }
    public static void updateOfflineState(final FirebaseUser currentFirebaseUser, final DatabaseReference mDatabaseReference){
        if (currentFirebaseUser!= null){
            mDatabaseReference.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_CONNECTION).setValue(false);
            mDatabaseReference.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime());
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild(currentFirebaseUser.getUid())){
                            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(ds.getKey()).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError!= null){
                                        Log.e("completion error", databaseError.getDetails());
                                    }
                                    else{

                                    }
                                }
                            });
                            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(ds.getKey()).child(currentFirebaseUser.getUid()).child(Constant.KEY_CONNECTION).setValue(false, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError!= null){
                                        Log.e("completion error", databaseError.getDetails());
                                    }
                                    else{

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
    }
    public static void updateOnlineState(final FirebaseUser currentFirebaseUser, final DatabaseReference mDatabaseReference){
        if (currentFirebaseUser!= null){
            mDatabaseReference.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_CONNECTION).setValue(true);
            mDatabaseReference.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime());

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild(currentFirebaseUser.getUid())){
                            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(ds.getKey()).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError!= null){
                                        Log.e("completion error", databaseError.getDetails());
                                    }
                                    else{

                                    }
                                }
                            });
                            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(ds.getKey()).child(currentFirebaseUser.getUid()).child(Constant.KEY_CONNECTION).setValue(true, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError!= null){
                                        Log.e("completion error", databaseError.getDetails());
                                    }
                                    else{

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
//            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime());
        }
    }
    public static void updateOnlineState(final String myUid, final DatabaseReference mDatabaseReference){
        if (myUid!= null){
            mDatabaseReference.child(Constant.CHILD_USERS).child(myUid).child(Constant.KEY_CONNECTION).setValue(true);
            mDatabaseReference.child(Constant.CHILD_USERS).child(myUid).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime());

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.hasChild(myUid)){
                            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(ds.getKey()).child(myUid).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError!= null){
                                        Log.e("completion error", databaseError.getDetails());
                                    }
                                    else{

                                    }
                                }
                            });
                            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(ds.getKey()).child(myUid).child(Constant.KEY_CONNECTION).setValue(true, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError!= null){
                                        Log.e("completion error", databaseError.getDetails());
                                    }
                                    else{

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
//            mDatabaseReference.child(Constant.CHILD_FRIENDLISTS).child(currentFirebaseUser.getUid()).child(Constant.KEY_LASTONLINE).setValue(CommonMethod.getCurrentTime());
        }
    }
    public static long convertDateToLong(Date date){
        long milliseconds = date.getTime();
        return milliseconds;
    }
//    public static boolean isConnectionToDatabase(final Context context){
//        final Boolean[] isConnected = {false};
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                Log.e("connected ---", connected+"");
//                isConnected[0] = connected;
//                Log.e("isconnected ---", isConnected[0]+"");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(context, R.string.annouce_database_has_problem, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if (isConnected[0].equals(false))
//            Toast.makeText(context, R.string.annouce_no_connection_to_database, Toast.LENGTH_SHORT).show();
//        return isConnected[0];
//    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm== null)
            Toast.makeText(context, R.string.announce_no_connection_to_network, Toast.LENGTH_SHORT).show();
        return cm.getActiveNetworkInfo() != null;
    }
    public static FirebaseUser getCurrentFirebaseUser(){
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser= firebaseAuth.getCurrentUser();
        return currentFirebaseUser;
    }
    public static void showAnimation(View view, Context context){

//        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation));

    }
    public static final String PREFS_NAME = "reservedKeyWord";

    public static final String KEY_COUNTRIES = "keywords";

    public static final int REQUEST_CODE = 1234;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

