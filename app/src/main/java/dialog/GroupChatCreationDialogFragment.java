package dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.UUID;

import common.CommonMethod;
import common.Constant;
import object.LastMessage;
import object.Message;
import object.Tag;
import object.User;
import view.ContactCompletionView;


public class GroupChatCreationDialogFragment extends DialogFragment {
    Button btSend, btCancel;
//    TextView tvTitle;
    EditText etInvitationMsg;

    DatabaseReference databaseReference;
    FirebaseUser currentFirebaseUser;
    User user;

    ArrayList<Tag> tags;
    ArrayList<Tag> selectedTags;
    ContactCompletionView completionView;
    ArrayAdapter<Tag> adapter;
    public GroupChatCreationDialogFragment(String myUid, String myname, String myPhotoUrl) {
        currentFirebaseUser= CommonMethod.getCurrentFirebaseUser();
        tags= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        selectedTags = new ArrayList<>();
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style of dialog fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SendInvitationDialog);
    }

    public GroupChatCreationDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        View view = inflater.inflate(R.layout.group_chat_creating, null);
        //init component
        btCancel = (Button) view.findViewById(R.id.btCancel);
        btSend = (Button) view.findViewById(R.id.btOK);
        etInvitationMsg = (EditText) view.findViewById(R.id.etInvitationMsg);
        completionView = (ContactCompletionView) view.findViewById(R.id.searchView);
        //auto complete view
        completionView.setTokenListener(new TokenCompleteTextView.TokenListener<Tag>() {
            @Override
            public void onTokenAdded(Tag token) {
                adapter.remove(token);
                adapter.notifyDataSetChanged();
                selectedTags.add(token);
            }

            @Override
            public void onTokenRemoved(Tag token) {
                adapter.add(token);
                adapter.notifyDataSetChanged();
                selectedTags.remove(token);
            }
        });
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tags);
        getTaglist();
        completionView.setAdapter(adapter);


        //handle event
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                String message = etInvitationMsg.getText().toString().trim();
                if (message.equals("")){
                    Toast.makeText(getActivity(), R.string.announce_empty_msg, Toast.LENGTH_SHORT).show();
                }
                else {
                    //push another participant info
                    if (selectedTags.size()==0){

                    }
                    else if (selectedTags.size()==1){
                        //send private message

                        String myUid= "";
                        if (currentFirebaseUser!= null)
                            myUid= currentFirebaseUser.getUid();
                        String clientUid= selectedTags.get(0).getUid();
                        databaseReference.child(Constant.CHILD_CHATONETOONE).child(myUid).child(clientUid).push().setValue(new Message(message, myUid), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(getActivity(), R.string.announce_cant_send_msg, Toast.LENGTH_SHORT).show();
                                }
                                else{

                                }
                            }
                        });
                        databaseReference.child(Constant.CHILD_FRIENDLISTS).child(myUid).child(clientUid).child(Constant.CHILD_LASTMESSAGE).setValue(new LastMessage(message,CommonMethod.getCurrentTime(), true,myUid), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(getActivity(), R.string.announce_cant_send_msg, Toast.LENGTH_SHORT).show();
                                }
                                else{

                                }
                            }
                        });
                        databaseReference.child(Constant.CHILD_CHATONETOONE).child(clientUid).child(myUid).push().setValue(new Message(message, myUid), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(getActivity(), R.string.announce_cant_send_msg, Toast.LENGTH_SHORT).show();
                                }
                                else{

                                }
                            }
                        });
                        databaseReference.child(Constant.CHILD_FRIENDLISTS).child(clientUid).child(myUid).child(Constant.CHILD_LASTMESSAGE).setValue(new LastMessage(message, myUid), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(getActivity(), R.string.announce_cant_send_msg, Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), R.string.announce_msg_is_sent, Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });
                    }
                    else{
                        //send group message
                        String uuid = UUID.randomUUID().toString();
                        for (Tag tag : selectedTags){

//                        Tag tag = (Tag) chip;
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid)
                                    .child(Constant.CHILD_USERS).child(tag.getUid()).child(Constant.KEY_NAME).setValue(tag.getName());
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid)
                                    .child(Constant.CHILD_USERS).child(tag.getUid()).child(Constant.KEY_PHOTOURL).setValue(tag.getPhotoUrl());
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid)
                                    .child(Constant.CHILD_USERS).child(tag.getUid()).child(Constant.KEY_STATUS).setValue(true);
                        }
                        //push my info
                        if (currentFirebaseUser!=null){
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid)
                                    .child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_NAME).setValue(currentFirebaseUser.getDisplayName());
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid)
                                    .child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_PHOTOURL).setValue(currentFirebaseUser.getPhotoUrl().toString());
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid)
                                    .child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_STATUS).setValue(true);
                            //push last message
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid).child(Constant.CHILD_LASTMESSAGE).setValue(
                                    new LastMessage(etInvitationMsg.getText().toString().trim(), currentFirebaseUser.getUid(), true));
                            //push name
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid).child(Constant.KEY_NAME).setValue("Unnamed");
                            //push message
                            databaseReference.child(Constant.CHILD_GROUPCHAT).child(uuid).child(Constant.KEY_MESSAGES).push().
                                    setValue(new Message(etInvitationMsg.getText().toString().trim(), currentFirebaseUser.getUid()), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError!= null){

                                            }else{
                                                Toast.makeText(getActivity(),R.string.announce_msg_is_sent, Toast.LENGTH_SHORT ).show();
                                                dismiss();
                                            }
                                        }
                                    });
                        }

                    }

                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                dismiss();
            }
        });
        return view;
    }



    public void getTaglist(){
        if (currentFirebaseUser!= null)
        {
            databaseReference.child(Constant.CHILD_FRIENDLISTS).
                    child(currentFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (tags!= null)
                        tags.clear();

                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        String uid = ds.getKey();
                        String name = "";
                        String photoUrl = "";
                        if (ds.hasChild(Constant.KEY_NAME)) {
                            name = ds.child(Constant.KEY_NAME).getValue().toString();
                        }
                        if (ds.hasChild(Constant.KEY_PHOTOURL)) {
                            photoUrl = ds.child(Constant.KEY_PHOTOURL).getValue().toString();
                        }
                        if (name.equals("") || photoUrl.equals("")) {

                        } else {
                            Tag tag = new Tag(uid, name, photoUrl);
                            adapter.add(tag);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
