package fragment;

/**
 * Created by root on 17/02/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.chi.heyfriendv21.R;
import com.chi.heyfriendv21.activity.GroupConversationActivity;
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



public class GroupChatFragment extends Fragment {

    private int progressStatus = 0;
    RadioButton rbPrivateChat, rbGroupChat;
    ListView lvGroupChat;
    DatabaseReference databaseReferenceUsers, databaseReferenceFriendLists;
    FirebaseUser currentFirebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    GroupChatForListViewAdapter groupChatForListViewAdapter;
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
        lvGroupChat = (ListView) view.findViewById(R.id.lvChatList);
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
        groupChatForListViewAdapter = new GroupChatForListViewAdapter(
                getActivity(), R.layout.item_private_chat,
                groupChatDatas, currentUid);
        lvGroupChat.setAdapter(groupChatForListViewAdapter);
        lvGroupChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (currentFirebaseUser!=null){
                    if (currentFirebaseUser!=null){
                    Intent intent= new Intent(getActivity(), GroupConversationActivity.class);
                    intent.putExtra(Constant.GROUP_CHAT_DATA, groupChatForListViewAdapter.getItem(position));
                    intent.putExtra(Constant.MY_UID, currentFirebaseUser.getUid());

                    startActivity(intent);
                }
                }

            }
        });
        getGroupMessage();
        return view;
    }

    private void getGroupMessage(){
        databaseReferenceFriendLists.getRoot().child(Constant.CHILD_GROUPCHAT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (currentFirebaseUser!= null){
                    groupChatDatas.clear();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (ds.child(Constant.CHILD_USERS).hasChild(currentFirebaseUser.getUid())){

                            boolean myStatus = false;
                            if (ds.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).
                                    hasChild(Constant.KEY_STATUS)){
                                myStatus= (boolean)ds.child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).
                                        child(Constant.KEY_STATUS).getValue();
                            }

                            if (myStatus==true){
                                GroupChatData groupChatData = new GroupChatData();
                                groupChatData.setUid(ds.getKey());
                                if (ds.hasChild(Constant.CHILD_LASTMESSAGE)){
                                    LastMessage lastMessage = ds.child(Constant.CHILD_LASTMESSAGE).getValue(LastMessage.class);
//                            Log.e("last message", lastMessage.getContent() + "   "+ lastMessage.getTime() + "   ");
                                    groupChatData.setLastMessage(lastMessage);
                                }
                                if (ds.hasChild(Constant.KEY_NAME)){
                                    groupChatData.setName(ds.child(Constant.KEY_NAME).getValue().toString());
//                            Log.e("name----", groupChatData.getName());
                                }
                                if (ds.hasChild(Constant.CHILD_USERS)){

                                    ArrayList<GroupChatData.Participant> participants= new ArrayList<>();
                                    for (DataSnapshot p : ds.child(Constant.CHILD_USERS).getChildren()){
                                        String name="", uid, photoUrl="";
                                        Boolean status= true;
                                        uid = p.getKey().toString();
//                                Log.e("uid--", uid);

                                        if (p.hasChild(Constant.KEY_NAME)){
                                            name = p.child(Constant.KEY_NAME).getValue().toString();
//                                    Log.e("name--", name);
                                        }
                                        if (p.hasChild(Constant.KEY_PHOTOURL)){
                                            photoUrl = p.child(Constant.KEY_PHOTOURL).getValue().toString();
//                                    Log.e("photourl---", photoUrl);
                                        }
                                        if (p.hasChild(Constant.KEY_STATUS)) {
                                            status = (boolean)p.child(Constant.KEY_STATUS).getValue();
                                        }
                                        if (name.equals("") || uid.equals("") || photoUrl.equals("")){
//                                    Log.e("name.equals..", "false");
                                        }
                                        else participants.add(new GroupChatData.Participant(uid, photoUrl, name, status));
                                    }
                                    groupChatData.setParticipants(participants);
                                }
//                        if (ds.child(Constant.CHILD_USERS).hasChild(currentFirebaseUser.getUid()))
                                groupChatDatas.add(groupChatData);

                            }

                        }
                    }
                }

                Collections.sort(groupChatDatas);
//                Log.e("group chat size", groupChatDatas.size()+"");

                groupChatForListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
