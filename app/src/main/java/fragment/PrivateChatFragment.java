package fragment;

/**
 * Created by root on 17/02/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chi.heyfriendv21.R;
import com.chi.heyfriendv21.activity.OneToOneConversationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import adapter.GroupChatForListViewAdapter;
import adapter.PrivateChatForListViewAdapter;
import common.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import object.Friend;
import object.GroupChatData;
import object.LastMessage;



public class PrivateChatFragment extends Fragment {

    private int progressStatus = 0;
    RadioButton rbPrivateChat, rbGroupChat;
    ListView lvGroupChat, lvPrivateChat;
    DatabaseReference databaseReferenceUsers, databaseReferenceFriendLists;
    FirebaseUser currentFirebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    GroupChatForListViewAdapter groupChatForListViewAdapter;
    PrivateChatForListViewAdapter privateChatForListViewAdapter;
    ArrayList<Friend> friends;
    ArrayList<String> listUidFriend;
    ArrayList<GroupChatData> groupChatDatas;
    AdapterView.AdapterContextMenuInfo info;
    CircleImageView civFloatButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_in_fragment_message, container, false);
        lvPrivateChat = (ListView) view.findViewById(R.id.lvChatList);
//        //Float button
//        civFloatButton = (CircleImageView) view.findViewById(R.id.civ_float_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUsers = firebaseDatabase.getReference().child(Constant.CHILD_USERS);
        databaseReferenceFriendLists= firebaseDatabase.getReference().child(Constant.CHILD_FRIENDLISTS);
        firebaseAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = firebaseAuth.getCurrentUser();

        listUidFriend = new ArrayList<String>();
        friends = new ArrayList<Friend>();
        groupChatDatas = new ArrayList<>();
        String currentUid= "";
        if (currentFirebaseUser!= null){
            currentUid= currentFirebaseUser.getUid();
        }
        privateChatForListViewAdapter = new PrivateChatForListViewAdapter(getActivity(), R.layout.item_private_chat, friends,
                currentUid);
        lvPrivateChat.setAdapter(privateChatForListViewAdapter);
        lvPrivateChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (currentFirebaseUser!=null){
                    Intent intent= new Intent(getActivity(), OneToOneConversationActivity.class);
                    intent.putExtra(Constant.CLIENT_UID, privateChatForListViewAdapter.getData().get(position).getUid());
                    intent.putExtra(Constant.MY_UID, currentFirebaseUser.getUid());
                    intent.putExtra("My " +Constant.KEY_PHOTOURL, currentFirebaseUser.getPhotoUrl().toString());
                    intent.putExtra("Client " +Constant.KEY_PHOTOURL, privateChatForListViewAdapter.getData().get(position).getPhotoURL());
                    intent.putExtra("client name", privateChatForListViewAdapter.getData().get(position).getName().toString());
                    intent.putExtra("my name", currentFirebaseUser.getDisplayName().toString());
                    startActivity(intent);
                }

            }
        });
        getPrivateMessages();
        return view;
    }

    private void getPrivateMessages(){
        ValueEventListener valueEventListenerPrivateMessage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (friends!=null)
                    friends.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.hasChild(Constant.CHILD_LASTMESSAGE)){
                        Friend friend = new Friend();
                        friend.setUid(ds.getKey());
                        String name="", photoUrl="";
                        String content="", senderUid="";
                        Boolean status=false;
                        long time=0;
                        if (ds.hasChild(Constant.KEY_NAME))
                            name=ds.child(Constant.KEY_NAME).getValue().toString();
                        if (ds.hasChild(Constant.KEY_PHOTOURL))
                            photoUrl= ds.child(Constant.KEY_PHOTOURL).getValue().toString();

                        if (ds.child(Constant.CHILD_LASTMESSAGE).hasChild(Constant.KEY_CONTENT)){
                            content= ds.child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_CONTENT).getValue().toString();
                        }
                        if (ds.child(Constant.CHILD_LASTMESSAGE).hasChild(Constant.KEY_TIME))
                            time = Long.parseLong(ds.child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_TIME).getValue().toString());
                        if (ds.child(Constant.CHILD_LASTMESSAGE).hasChild(Constant.KEY_STATUS))
                            status = (Boolean) ds.child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_STATUS).getValue();
                        if (ds.child(Constant.CHILD_LASTMESSAGE).hasChild(Constant.KEY_SENDERUID))
                            senderUid = ds.child(Constant.CHILD_LASTMESSAGE).child(Constant.KEY_SENDERUID).getValue().toString();
                        if (name.equals("") && photoUrl.equals("")
                                && content.equals("") && senderUid.equals("")
                                && status==false){
                        }
                        else{
                            friend.setPhotoURL(photoUrl);
                            friend.setName(name);
                            friend.setLastMessage(new LastMessage(content,time, status, senderUid));
                            friends.add(friend);
                        }

                    }
                }
                Collections.sort(friends);
                privateChatForListViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        if (currentFirebaseUser!=null){
            databaseReferenceFriendLists.child(currentFirebaseUser.getUid()).addValueEventListener(valueEventListenerPrivateMessage);
        }

    }
}
