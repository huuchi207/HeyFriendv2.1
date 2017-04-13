package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chi.heyfriendv21.R;
import com.chi.heyfriendv21.activity.GroupConversationActivity;
import com.chi.heyfriendv21.activity.MainActivity;
import com.chi.heyfriendv21.activity.OneToOneConversationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import common.Constant;
import object.GroupChatData;
import object.Invitation;
import object.LastMessage;

public class ReceiveMessageService extends Service {
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentFirebaseUser;
    public ReceiveMessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate(){
        super.onCreate();

        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth= firebaseAuth.getInstance();
        currentFirebaseUser= firebaseAuth.getCurrentUser();
        //listen for private message
        if (currentFirebaseUser!= null){
            return;
        }
        databaseReference.child(Constant.CHILD_FRIENDLISTS).child(currentFirebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChild(Constant.CHILD_LASTMESSAGE)){
                    LastMessage lastMessage= dataSnapshot.child(Constant.CHILD_LASTMESSAGE).getValue(LastMessage.class);
                    if (!lastMessage.isStatus()){
                        String clientUid= dataSnapshot.getKey().toString();
                        String myUid = currentFirebaseUser.getUid();
                        String myName= currentFirebaseUser.getDisplayName();
                        String clientName= "";
                        String content= "";
                        String myPhotoUrl= "";
                        String clientPhotoUrl="";
                        if (dataSnapshot.hasChild(Constant.KEY_NAME))
                            clientName= dataSnapshot.child(Constant.KEY_NAME).getValue().toString();
                        if(dataSnapshot.child(Constant.CHILD_LASTMESSAGE).hasChild(Constant.KEY_CONTENT))
                            content= dataSnapshot.child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_CONTENT).getValue().toString();
                        myPhotoUrl= currentFirebaseUser.getPhotoUrl().toString();
                        if(dataSnapshot.hasChild(Constant.KEY_PHOTOURL))
                            clientPhotoUrl= dataSnapshot.child(Constant.KEY_PHOTOURL).getValue().toString();
                        int hashCode = dataSnapshot.getKey().toString().hashCode();
                        if (clientName.equals("") || content.equals("")
                                || myPhotoUrl.equals("") || clientPhotoUrl.equals("")){

                        }else{
                            Intent resultIntent = new Intent(ReceiveMessageService.this, OneToOneConversationActivity.class);
                            resultIntent.putExtra(Constant.CLIENT_UID, clientUid);
                            resultIntent.putExtra(Constant.MY_UID, myUid);
                            resultIntent.putExtra("My " +Constant.KEY_PHOTOURL,myPhotoUrl);
                            resultIntent.putExtra("Client " +Constant.KEY_PHOTOURL,clientPhotoUrl);
                            resultIntent.putExtra("client name", clientName);
                            resultIntent.putExtra("my name", myName);
                            pushNoti(clientName, content , hashCode, resultIntent);
                        }

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChild(Constant.CHILD_LASTMESSAGE)){
                    LastMessage lastMessage= dataSnapshot.child(Constant.CHILD_LASTMESSAGE).getValue(LastMessage.class);
                    if (!lastMessage.isStatus()){
                        String clientUid= dataSnapshot.getKey().toString();
                        String myUid = currentFirebaseUser.getUid();
                        String myName= currentFirebaseUser.getDisplayName();
                        String clientName= "";
                        String content= "";
                        String myPhotoUrl= "";
                        String clientPhotoUrl="";
                        if (dataSnapshot.hasChild(Constant.KEY_NAME))
                            clientName= dataSnapshot.child(Constant.KEY_NAME).getValue().toString();
                        if(dataSnapshot.child(Constant.CHILD_LASTMESSAGE).hasChild(Constant.KEY_CONTENT))
                            content= dataSnapshot.child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_CONTENT).getValue().toString();
                        myPhotoUrl= currentFirebaseUser.getPhotoUrl().toString();
                        if(dataSnapshot.hasChild(Constant.KEY_PHOTOURL))
                            clientPhotoUrl= dataSnapshot.child(Constant.KEY_PHOTOURL).getValue().toString();
                        int hashCode = dataSnapshot.getKey().toString().hashCode();
                        if (clientName.equals("") || content.equals("")
                                || myPhotoUrl.equals("") || clientPhotoUrl.equals("")){

                        }else{
                            Intent resultIntent = new Intent(ReceiveMessageService.this, OneToOneConversationActivity.class);
                            resultIntent.putExtra(Constant.CLIENT_UID, clientUid);
                            resultIntent.putExtra(Constant.MY_UID, myUid);
                            resultIntent.putExtra("My " +Constant.KEY_PHOTOURL,myPhotoUrl);
                            resultIntent.putExtra("Client " +Constant.KEY_PHOTOURL,clientPhotoUrl);
                            resultIntent.putExtra("client name", clientName);
                            resultIntent.putExtra("my name", myName);
                            pushNoti(clientName, content , hashCode, resultIntent);
                        }

                    }
                }
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
        //listen for group message
        databaseReference.child(Constant.CHILD_GROUPCHAT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot ds, String s) {
                if (ds.hasChild(Constant.CHILD_USERS)){
                    if(ds.child(Constant.CHILD_USERS).hasChild(currentFirebaseUser.getUid())){
                        GroupChatData groupChatData= new GroupChatData();
                        groupChatData.setUid(ds.getKey());
                        Boolean myStatus=true;
                        String senderName="";
                        Object o = ds.child(Constant.CHILD_USERS).
                                child(currentFirebaseUser.getUid()).child(Constant.KEY_STATUS).getValue();
                        if (o instanceof Boolean){
                            myStatus= (boolean) o;
                        }
                        Log.e("status receive msg", myStatus +"");
                        if (myStatus==true)
                            return;
                        if (ds.hasChild(Constant.CHILD_LASTMESSAGE)){
                            LastMessage lastMessage = ds.child(Constant.CHILD_LASTMESSAGE).getValue(LastMessage.class);
//                            Log.e("last message", lastMessage.getContent() + "   "+ lastMessage.getTime() + "   ");
                            groupChatData.setLastMessage(lastMessage);
                        }
                        if (ds.hasChild(Constant.CHILD_USERS)) {
                            ArrayList<GroupChatData.Participant> participants = new ArrayList<>();
                            for (DataSnapshot p : ds.child(Constant.CHILD_USERS).getChildren()) {

                                String name = "", uid, photoUrl = "";
                                Boolean status = false;
                                uid = p.getKey().toString();

                                if (p.hasChild(Constant.KEY_NAME)) {
                                    name = p.child(Constant.KEY_NAME).getValue().toString();
                                }
                                if (p.hasChild(Constant.KEY_PHOTOURL)) {
                                    photoUrl = p.child(Constant.KEY_PHOTOURL).getValue().toString();
                                }
                                if (p.hasChild(Constant.KEY_STATUS)) {
                                    status = (boolean)p.child(Constant.KEY_STATUS).getValue();
                                }
                                if(uid.equals(groupChatData.getLastMessage().getSenderUid()))
                                    senderName = name;
                                if (name.equals("") || uid.equals("") || photoUrl.equals("")) {
                                } else
                                    participants.add(new GroupChatData.Participant(name, uid, photoUrl, status));
                            }
                            groupChatData.setParticipants(participants);

                        if (ds.hasChild(Constant.KEY_NAME)){
                            groupChatData.setName(ds.child(Constant.KEY_NAME).getValue().toString());
//                            Log.e("name----", groupChatData.getName());
                        }

                        }
                        int hashCode = groupChatData.getUid().hashCode();
                        Intent intent= new Intent(ReceiveMessageService.this, GroupConversationActivity.class);
                        String converName="";
                        String content= senderName + ": "+groupChatData.getLastMessage().getContent();
                        if (groupChatData.getName()!= null)
                            converName = groupChatData.getName();
                        intent.putExtra(Constant.GROUP_CHAT_DATA, groupChatData);
                        intent.putExtra(Constant.MY_UID, currentFirebaseUser.getUid());
                        pushNoti(converName,content, hashCode, intent);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot ds, String s) {
                if (ds.hasChild(Constant.CHILD_USERS)){
                    if(ds.child(Constant.CHILD_USERS).hasChild(currentFirebaseUser.getUid())){
                        GroupChatData groupChatData= new GroupChatData();
                        groupChatData.setUid(ds.getKey());
                        Boolean myStatus=true;
                        String senderName="";
                        Object o = ds.child(Constant.CHILD_USERS).
                                child(currentFirebaseUser.getUid()).child(Constant.KEY_STATUS).getValue();
                        if (o instanceof Boolean){
                            myStatus= (boolean) o;
                        }
                        Log.e("status receive msg", myStatus +"");
                        if (myStatus==true)
                            return;
                        if (ds.hasChild(Constant.CHILD_LASTMESSAGE)){
                            LastMessage lastMessage = ds.child(Constant.CHILD_LASTMESSAGE).getValue(LastMessage.class);
//                            Log.e("last message", lastMessage.getContent() + "   "+ lastMessage.getTime() + "   ");
                            groupChatData.setLastMessage(lastMessage);
                        }
                        if (ds.hasChild(Constant.CHILD_USERS)) {
                            ArrayList<GroupChatData.Participant> participants = new ArrayList<>();
                            for (DataSnapshot p : ds.child(Constant.CHILD_USERS).getChildren()) {

                                String name = "", uid, photoUrl = "";
                                Boolean status = false;
                                uid = p.getKey().toString();

                                if (p.hasChild(Constant.KEY_NAME)) {
                                    name = p.child(Constant.KEY_NAME).getValue().toString();
                                }
                                if (p.hasChild(Constant.KEY_PHOTOURL)) {
                                    photoUrl = p.child(Constant.KEY_PHOTOURL).getValue().toString();
                                }
                                if (p.hasChild(Constant.KEY_STATUS)) {
                                    status = (boolean)p.child(Constant.KEY_STATUS).getValue();
                                }
                                if(uid.equals(groupChatData.getLastMessage().getSenderUid()))
                                    senderName = name;
                                if (name.equals("") || uid.equals("") || photoUrl.equals("")) {
                                } else
                                    participants.add(new GroupChatData.Participant(name, uid, photoUrl, status));
                            }
                            groupChatData.setParticipants(participants);

                            if (ds.hasChild(Constant.KEY_NAME)){
                                groupChatData.setName(ds.child(Constant.KEY_NAME).getValue().toString());
//                            Log.e("name----", groupChatData.getName());
                            }

                        }
                        int hashCode = groupChatData.getUid().hashCode();
                        Intent intent= new Intent(ReceiveMessageService.this, GroupConversationActivity.class);
                        String converName="";
                        String content= senderName + ": "+groupChatData.getLastMessage().getContent();
                        if (groupChatData.getName()!= null)
                            converName = groupChatData.getName();
                        intent.putExtra(Constant.GROUP_CHAT_DATA, groupChatData);
                        intent.putExtra(Constant.MY_UID, currentFirebaseUser.getUid());
                        pushNoti(converName,content, hashCode, intent);
                    }
                }
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
        //listen invitation message
        databaseReference.child(Constant.CHILD_INVITATIONS).child(currentFirebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Invitation invitation= dataSnapshot.getValue(Invitation.class);
                String senderName = invitation.getSender().getName();
                String content = invitation.getInvitationMessage();
                int hashCode = invitation.getSender().getUid().hashCode();
                Intent intent = new Intent(ReceiveMessageService.this, MainActivity.class);
                pushNoti(senderName,content, hashCode, intent);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Invitation invitation= dataSnapshot.getValue(Invitation.class);
                String senderName = invitation.getSender().getName();
                String content = invitation.getInvitationMessage();
                int hashCode = invitation.getSender().getUid().hashCode();
                Intent intent = new Intent(ReceiveMessageService.this, MainActivity.class);
                pushNoti(senderName,content, hashCode, intent);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Invitation invitation= dataSnapshot.getValue(Invitation.class);
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(invitation.getSender().getUid().hashCode());
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
    public int onStartCommand(Intent intent, int flags, int startId){

        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    public void pushNoti(String title, String content, int id, Intent resultIntent){
        NotificationCompat.Builder mBuilder;
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(ReceiveMessageService.this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_ONE_SHOT);
        mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setLights(Color.BLUE, 500, 500).setContentText(content)
                .setVibrate(new long[] { 100, 250, 100, 250, 100, 250 })
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = mBuilder.build();

        mNotificationManager.notify(id, noti);
    }
}
