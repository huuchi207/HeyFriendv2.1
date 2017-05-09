package com.chi.heyfriendv21.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import adapter.PrivateMessagesForRecyclerViewAdapter;
import common.CommonMethod;
import common.Constant;
import common.MessageViewHolder;
import object.LastMessage;
import object.Message;



public class OneToOneConversationActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_IMAGE_CAPTURE = 111;


    private String clientUid, myUid;
    private String clientPhotoURL, myPhotoURL, clientName, myName;
    private ArrayList<Message> messages;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    ImageView ibBack;
    ImageView  ivCamera;
    TextView tvName, tvState;
    Button btMenu;
    RecyclerView recyclerView;
    private PrivateMessagesForRecyclerViewAdapter privateMessagesForRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ImageView ivSend;
    private EditText etMessage;
    private ProgressDialog loadingProgressBar = null;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = privateMessagesForRecyclerViewAdapter.getPosition();
        } catch (Exception e) {
//            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 1:
                // do your stuff

                break;
            case 2:
                // copy text
                if (position!= -1){
                    ClipboardManager clipboard = (ClipboardManager) OneToOneConversationActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    String text = privateMessagesForRecyclerViewAdapter.getItem(position).getMessage();
                    ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                    clipboard.setPrimaryClip(clip);

                }
                break;
            case 3:
                //delete message
                if (position!= -1){
                    final int finalPosition = position;
                    if (position== privateMessagesForRecyclerViewAdapter.getItemCount()-1
                            && privateMessagesForRecyclerViewAdapter.getItemCount()>1){
//                        Log.e("----------index", position+"");
                        Message prevMessage = privateMessagesForRecyclerViewAdapter.getItem(position-1);

                        databaseReference.child(Constant.CHILD_FRIENDLISTS).child(myUid).child(clientUid).
                                child(Constant.CHILD_LASTMESSAGE).setValue(new LastMessage(prevMessage.getMessage(), prevMessage.getTime(), true, prevMessage.getSenderUid()), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(OneToOneConversationActivity.this,getString(R.string.announce_cant_delete_msg), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    deleteMessage(finalPosition);
                                }
                            }
                        });
                    }
                    else
                    if (position==0 && privateMessagesForRecyclerViewAdapter.getItemCount()==1){
                        databaseReference.child(Constant.CHILD_FRIENDLISTS).child(myUid).child(clientUid).
                                child(Constant.CHILD_LASTMESSAGE).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(OneToOneConversationActivity.this,getString(R.string.announce_cant_delete_msg), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    deleteMessage(finalPosition);
                                }
                            }
                        });
                    }
                    else {
                        deleteMessage(position);
                    }
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onetoone_conversation);

        initViews();
        getDataIntent();
        getClientInfo();
        CommonMethod.updateOnlineState(myUid, databaseReference);
        privateMessagesForRecyclerViewAdapter = new
                PrivateMessagesForRecyclerViewAdapter(Message.class, R.layout.item_message_onetoone, MessageViewHolder.class,
                databaseReference.child(Constant.CHILD_CHATONETOONE).
                        child(myUid).child(clientUid).getRef(),
                OneToOneConversationActivity.this, clientUid, myUid, clientPhotoURL, this);

        privateMessagesForRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = privateMessagesForRecyclerViewAdapter.getItemCount();
                int lastVisiblePosition =linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(privateMessagesForRecyclerViewAdapter);

        //auto scroll to bottom
        if (Build.VERSION.SDK_INT >= 11) {
            recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    }
                }
            });
        }
    }
    public void deleteMessage(int position){
        final MessageViewHolder messageViewHolder= (MessageViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        messageViewHolder.llAllComponent.setVisibility(View.GONE);
        privateMessagesForRecyclerViewAdapter.getRef(position).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!= null){
                    Toast.makeText(OneToOneConversationActivity.this,getString(R.string.announce_cant_delete_msg), Toast.LENGTH_SHORT).show();
                    messageViewHolder.llAllComponent.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(OneToOneConversationActivity.this, R.string.announce_msg_is_deleted, Toast.LENGTH_SHORT).show();
                }
            }
        });

//        messageViewHolder.tvDateSending.setVisibility(View.GONE);

    }
    public void initViews(){
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
//        ivMap = (ImageView) findViewById(R.id.ivLocation);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        tvName = (TextView) findViewById(R.id.tvName);
        tvState= (TextView) findViewById(R.id.tvState);
        etMessage = (EditText) findViewById(R.id.etMessage);
        recyclerView= (RecyclerView) findViewById(R.id.messageRecyclerView);
        btMenu = (Button) findViewById(R.id.btMenu);
        loadingProgressBar = new ProgressDialog(this);
        loadingProgressBar.setCancelable(false);
        loadingProgressBar.setMessage(getString(R.string.txt_uploading));
        loadingProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //set on click button
//        ivMap.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        btMenu.setOnClickListener(this);
        btMenu.setVisibility(View.GONE);
        ivSend.setEnabled(false);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                   ivSend.setEnabled(true);
                }
                else {
                    ivSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        linearLayoutManager= new LinearLayoutManager(this);
        updateView();

    }
    public void getDataIntent(){
        Intent intent = getIntent();
        clientUid= intent.getStringExtra(Constant.CLIENT_UID);
        myUid = intent.getStringExtra(Constant.MY_UID);
        myPhotoURL= intent.getStringExtra("My " +Constant.KEY_PHOTOURL);
        clientPhotoURL= intent.getStringExtra("Client " +Constant.KEY_PHOTOURL);
        clientName = intent.getStringExtra("client name");
        myName =intent.getStringExtra("my name");

    }

    public void getClientInfo(){
        ValueEventListener valueEventListenerClientInfo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(clientUid)){
                    String name="", state="";
                    Boolean connection= false;
                    long lastOnline=0;
                    if (dataSnapshot.child(clientUid).hasChild(Constant.KEY_NAME))
                        name= dataSnapshot.child(clientUid).child(Constant.KEY_NAME).getValue().toString();
                    if (tvName != null)
                        tvName.setText(name);
                    if (dataSnapshot.child(clientUid).hasChild(Constant.KEY_CONNECTION))
                        connection= (Boolean) dataSnapshot.child(clientUid).child(Constant.KEY_CONNECTION).getValue();
                    if (dataSnapshot.child(clientUid).hasChild(Constant.KEY_LASTONLINE))
                        lastOnline= Long.parseLong(dataSnapshot.child(clientUid).child(Constant.KEY_LASTONLINE).getValue().toString());
                    if (connection==false){
                        state=getString(R.string.txt_state)+ " "+CommonMethod.diffTime(getApplicationContext(), lastOnline);
                    }
                    else state = "Online";
                    if (tvState!=null)
                       tvState.setText(state);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child(Constant.CHILD_USERS).addValueEventListener(valueEventListenerClientInfo);
    }
    @Override
    public void onClick(View view) {
//        view.startAnimation(Constant.buttonClick);
        CommonMethod.showAnimation(view, OneToOneConversationActivity.this);
        if (view == ibBack)

            finish();
        else if (view == ivSend){
            //update in CHILD_CHATONETOONE and CHILD_LASTMESSAGE
            pushMessage("");

        }else if (view == ivCamera){
            onLaunchCamera();
        }
//        else if(view == ivMap){
//            Intent intent=new Intent(OneToOneConversationActivity.this,MainActivity.class);
//            intent.putExtra("fragment","track");
//            intent.putExtra(Constant.KEY_SEND_CLIENT,clientUid);
//            startActivity(intent);
//            finish();
//        }
//        else if (view== ibMenu){
////            showPopup(view);
//        }
    }
    private void updateView(){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(30000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getClientInfo();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            postDataToFirebase(imageBitmap);
        }
    }
    private void postDataToFirebase(Bitmap bitmap) {
        String msg = etMessage.getText().toString();
        if (bitmap!= null) {
            loadingProgressBar.show();
            String fileName = ""+bitmap.hashCode();
            StorageReference filepathRef =
                    FirebaseStorage.getInstance().getReference("images"+ "/"+ fileName);

            //compress image
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = filepathRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    pushMessage(downloadUrl.toString());
                    loadingProgressBar.dismiss();

                }
            });

        }
    }
    private void pushMessage(String photoUrl){
        String msg = etMessage.getText().toString().trim();
        if (msg.equals("") &&photoUrl.equals("")){
            Toast.makeText(OneToOneConversationActivity.this, getString(R.string.announce_empty_msg),
                    Toast.LENGTH_SHORT).show();
        }else{
            //for show last message in chat list
            if (msg.equals(""))
                msg = "Image";
            //ignore photoUrl path in firebase storage
            if (photoUrl.equals(""))
                photoUrl= null;
            databaseReference.child(Constant.CHILD_CHATONETOONE+"/"+myUid+"/"+clientUid).
                    push().setValue(new Message(msg, myUid, photoUrl));
            databaseReference.child(Constant.CHILD_FRIENDLISTS+"/"+myUid+"/"+clientUid+"/"
                    +Constant.CHILD_LASTMESSAGE).
                    setValue(new LastMessage(msg, myUid, true));
            databaseReference.child(Constant.CHILD_CHATONETOONE+"/"+clientUid+"/"+myUid).push().
                    setValue(new Message(msg, myUid, photoUrl));
            databaseReference.child(Constant.CHILD_FRIENDLISTS+"/"+clientUid+"/"+myUid+"/"+
                    Constant.CHILD_LASTMESSAGE).
                    setValue(new LastMessage(msg, myUid));
        }
        etMessage.setText("");
    }
}
