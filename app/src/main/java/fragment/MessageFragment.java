package fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
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
import adapter.MessageFragmentPagerAdapter;
import adapter.PrivateChatForListViewAdapter;
import common.CommonMethod;
import common.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import dialog.GroupChatCreationDialogFragment;
import object.Friend;
import object.GroupChatData;
import object.LastMessage;

/**
 * Created by huuchi207 on 17/10/2016.
 */

public class MessageFragment extends Fragment implements View.OnClickListener {
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
    private FragmentActivity myContext;
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReferenceUsers = firebaseDatabase.getReference().child(Constant.CHILD_USERS);
//        databaseReferenceFriendLists= firebaseDatabase.getReference().child(Constant.CHILD_FRIENDLISTS);
//        firebaseAuth = FirebaseAuth.getInstance();
//        currentFirebaseUser = firebaseAuth.getCurrentUser();
//
//        listUidFriend = new ArrayList<String>();
//        friends = new ArrayList<Friend>();
//        groupChatDatas = new ArrayList<>();
//        String currentUid= "";
//        if (currentFirebaseUser!= null){
//            currentUid= currentFirebaseUser.getUid();
//        }
//        privateChatForListViewAdapter = new PrivateChatForListViewAdapter(getActivity(), R.layout.item_private_chat, friends,
//                currentUid);
//        groupChatForListViewAdapter = new GroupChatForListViewAdapter(getActivity(), R.layout.item_private_chat,
//                groupChatDatas, currentUid);
//
////        getPrivateMessages();
//        getGroupMessage();
////        updateView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        super.onCreateContextMenu(menu, v, menuInfo);
////        menu.setHeaderTitle("Choose Action");   // Context-menu title
//        menu.add(0, 1, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getTitle()== "Delete"){
//            final int position = info.position;
//            final Friend f= privateChatForListViewAdapter.getItem(position);
//            if (currentFirebaseUser!=null && f!= null)
//            databaseReferenceFriendLists.getRoot().child(Constant.CHILD_CHATONETOONE)
//                    .child(currentFirebaseUser.getUid()).child(f.getUid()).removeValue(new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                    if (databaseError!= null){
//                        Toast.makeText(getActivity(), R.string.announce_cant_delete_this_conversation, Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        databaseReferenceFriendLists.getRoot().child(Constant.CHILD_FRIENDLISTS)
//                                .child(currentFirebaseUser.getUid()).child(f.getUid()).child(Constant.CHILD_LASTMESSAGE).removeValue(new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                if (databaseError!= null){
//                                    Toast.makeText(getActivity(), R.string.announce_cant_delete_this_conversation, Toast.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    Toast.makeText(getActivity(), R.string.announce_this_conversation_is_deleted, Toast.LENGTH_SHORT).show();
////                                    friends.remove(position);
////                                    privateChatForListViewAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//
//                    }
//                }
//            });
//        }
        return true;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_2, container, false);
//        rbGroupChat = (RadioButton) view.findViewById(R.id.rbGroupChat);
//        rbPrivateChat = (RadioButton) view.findViewById(R.id.rbPrivateChat);
//        lvGroupChat = (ListView) view.findViewById(R.id.lvGroupChat);
//        lvPrivateChat = (ListView) view.findViewById(R.id.lvPrivateChat);
//        //Float button
//        civFloatButton = (CircleImageView) view.findViewById(R.id.civ_float_button);
//        lvPrivateChat.setAdapter(privateChatForListViewAdapter);
//        lvPrivateChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (currentFirebaseUser!=null){
//                    Intent intent= new Intent(getActivity(), OneToOneConversationActivity.class);
//                    intent.putExtra(Constant.CLIENT_UID, privateChatForListViewAdapter.getData().get(position).getUid());
//                    intent.putExtra(Constant.MY_UID, currentFirebaseUser.getUid());
//                    intent.putExtra("My " +Constant.KEY_PHOTOURL, currentFirebaseUser.getPhotoUrl().toString());
//                    intent.putExtra("Client " +Constant.KEY_PHOTOURL, privateChatForListViewAdapter.getData().get(position).getPhotoURL());
//                    intent.putExtra("client name", privateChatForListViewAdapter.getData().get(position).getName().toString());
//                    intent.putExtra("my name", currentFirebaseUser.getDisplayName().toString());
//                    startActivity(intent);
//                }
//
//            }
//        });
//        registerForContextMenu(lvPrivateChat);
//
////        registerForContextMenu(lvGroupChat);
//        lvGroupChat.setAdapter(groupChatForListViewAdapter);
//        lvGroupChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (currentFirebaseUser!=null){
//                    Intent intent= new Intent(getActivity(), GroupConversationActivity.class);
//                    intent.putExtra(Constant.GROUP_CHAT_DATA, groupChatForListViewAdapter.getItem(position));
//                    intent.putExtra(Constant.MY_UID, currentFirebaseUser.getUid());
//
//                    startActivity(intent);
//                }
//
//            }
//        });
//
//        rbPrivateChat.setOnClickListener(this);
//        rbGroupChat.setOnClickListener(this);
//        civFloatButton.setOnClickListener(this);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        MessageFragmentPagerAdapter p =new MessageFragmentPagerAdapter(myContext.getSupportFragmentManager(), getActivity());
        viewPager.setAdapter(p);

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tab_main_pager_sliding);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        return view;
    }



    @Override
    public void onClick(View view) {
//        if(view==rbGroupChat){
//            lvPrivateChat.setVisibility(View.GONE);
//            lvGroupChat.setVisibility(View.VISIBLE);
//        }
//        else if(view == rbPrivateChat){
//            lvGroupChat.setVisibility(View.GONE);
//            lvPrivateChat.setVisibility(View.VISIBLE);
//        }
//        else if (view == civFloatButton){
//            FirebaseUser firebaseUser= CommonMethod.getCurrentFirebaseUser();
//            if(firebaseUser!=null){
//                CommonMethod.showAnimation(view, getContext());
//                GroupChatCreationDialogFragment dialogFragment=
//                        new GroupChatCreationDialogFragment(
//                                firebaseUser.getUid(),
//                                firebaseUser.getDisplayName(),
//                                firebaseUser.getPhotoUrl().toString()
//                        );
//                dialogFragment.show(getActivity().getFragmentManager(), "");
//            }
//
//        }
    }
    private void updateView(){

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(30000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lvPrivateChat.invalidateViews();
                                lvGroupChat.invalidateViews();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
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
