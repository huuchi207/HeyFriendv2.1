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
        return true;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_2, container, false);

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

}
