package com.chi.heyfriendv21.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;
import com.firebase.client.Firebase;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import adapter.ListFriendAdapter;
import adapter.SearchFriendAdapter;
import common.CommonMethod;
import common.Constant;

import de.hdodenhof.circleimageview.CircleImageView;
import dialog.GroupChatCreationDialogFragment;
import dialog.NewStatusDialogFragment;
import dialog.UserInfoDialogFragment;
import fragment.InvitationFragment;
import fragment.MessageFragment;
import fragment.SettingFragment;
import fragment.TimelineFragment;
import object.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{
    private static final String TAG = "MAIN_ACTIVITY";
    private MenuItem menuItemWaiting;
    private DrawerLayout drawerLayout;

    private ImageButton ibNav, ibSearch;
    private Button btAction;
    private View shadowActionBar;
    private MaterialRippleLayout rippleLayoutForActionButton, rippleLayoutForSearchButton;
//    private ArrayList<String> allUsers;
   private  ArrayList<User> allUsers= new ArrayList<>();
    private Map<String, Boolean> isAFriend;
    private SearchFriendAdapter searchFriendAdapter;

    private NavigationView navigationView;

    private ListView lvFriendsOnline, lvFriendsOffline;
    private ListFriendAdapter friendsOnlineAdapter, friendsOfflineAdapter;
    private ArrayList friendsOnline, friendsOffline;

    private TextView tvTotalFriends;
    private ProgressBar progressBarListFriend;
    // FAB
    private com.getbase.floatingactionbutton.FloatingActionsMenu floatingActionsMenuFAB;
    private com.getbase.floatingactionbutton.FloatingActionButton fabNew, fabMessage;
    private View viewBlur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                FirebaseUser firebaseUser= CommonMethod.getCurrentFirebaseUser();
//                if(firebaseUser!=null){
//                    GroupChatCreationDialogFragment dialogFragment=
//                            new GroupChatCreationDialogFragment(
//                                    firebaseUser.getUid(),
//                                    firebaseUser.getDisplayName(),
//                                    firebaseUser.getPhotoUrl().toString()
//                            );
//                    dialogFragment.show(getFragmentManager(), "");
//                }
//
//            }
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //add divider to item nav view
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        //init drawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(menuItemWaiting != null) {
                    onNavigationItemSelected(menuItemWaiting);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //check firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.e(TAG, "User is signed in");
        } else {
            // User is signed out
            Log.e(TAG, "User is signed out");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        //listen changing user
        FirebaseAuth.AuthStateListener mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "User is signed in");
                } else {
                    // User is signed out
                    Log.e(TAG, "User is signed out");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                // ...
            }
        };

        //init views
        btAction = (Button) findViewById(R.id.btHelp);
        ibNav = (ImageButton) findViewById(R.id.ibNav);
        ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        shadowActionBar = findViewById(R.id.shadow_actionbar);
        rippleLayoutForActionButton = (MaterialRippleLayout) findViewById(R.id.ripple_view_action_button);
        rippleLayoutForSearchButton = (MaterialRippleLayout) findViewById(R.id.ripple_view_search_button);
        //init view for friendlist
        lvFriendsOffline = (ListView) findViewById(R.id.list_friend_offline);
        lvFriendsOnline = (ListView) findViewById(R.id.list_friend_online);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tvTotalFriends = (TextView) findViewById(R.id.tvTotalFriends);
        progressBarListFriend = (ProgressBar) findViewById(R.id.progress_bar_list_friend);
        isAFriend = new HashMap<>();
        friendsOffline = new ArrayList();
        friendsOnline = new ArrayList();

        friendsOfflineAdapter = new ListFriendAdapter(this, R.layout.itemlist_friendoffline,friendsOffline);
        friendsOnlineAdapter = new ListFriendAdapter(this, R.layout.itemlist_friendonline, friendsOnline);

        lvFriendsOffline.setAdapter(friendsOfflineAdapter);
        lvFriendsOnline.setAdapter(friendsOnlineAdapter);
        //-----------------------------
        //init view for search friend
        searchFriendAdapter = new SearchFriendAdapter(this, R.layout.item_search_friend,allUsers);
        //-----------------------------
        //view for fab
        floatingActionsMenuFAB = (com.getbase.floatingactionbutton.FloatingActionsMenu)
                findViewById(R.id.multiple_actions_fab);
        fabMessage = (com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.fabMessage);
        fabNew =(com.getbase.floatingactionbutton.FloatingActionButton)
                findViewById(R.id.fabNew);
        viewBlur=  findViewById(R.id.viewBlur);
        viewBlur.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(floatingActionsMenuFAB.isExpanded())
                    floatingActionsMenuFAB.collapse();
                viewBlur.setVisibility(View.GONE);
                return true;
            }
        });
        floatingActionsMenuFAB.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                viewBlur.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                viewBlur.setVisibility(View.GONE);
            }
        });

        //-----------------------------

        //assign onClickListener
        btAction.setOnClickListener(this);
        ibNav.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
        fabNew.setOnClickListener(this);
        fabMessage.setOnClickListener(this);
        //set current fragment
        switchFragmentById(R.id.nav_timeline);
        //get data from server
        getAllUser();
        //
        setDataToNavHeader();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        menuItemWaiting = null;
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuItemWaiting = item;
            drawerLayout.closeDrawers();
            return false;
        }

        switchFragmentById(id);
        return true;

    }
    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    private void switchFragmentById(int id) {
        //set actived item nav
        navigationView.getMenu().findItem(id).setChecked(true);
        if (id == R.id.nav_timeline) {
            showFragment(new TimelineFragment());
            shadowActionBar.setVisibility(View.VISIBLE);
            rippleLayoutForActionButton.setVisibility(View.VISIBLE);
            rippleLayoutForSearchButton.setVisibility(View.GONE);
            floatingActionsMenuFAB.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_conversation){
            showFragment(new MessageFragment());
            shadowActionBar.setVisibility(View.GONE);
            rippleLayoutForActionButton.setVisibility(View.VISIBLE);
            rippleLayoutForSearchButton.setVisibility(View.GONE);
            floatingActionsMenuFAB.setVisibility(View.VISIBLE);
        }
        else if (id== R.id.nav_invitation){
            showFragment(new InvitationFragment());
            shadowActionBar.setVisibility(View.VISIBLE);
            rippleLayoutForActionButton.setVisibility(View.GONE);
            rippleLayoutForSearchButton.setVisibility(View.VISIBLE);
            floatingActionsMenuFAB.setVisibility(View.GONE);
        }
        else if (id==R.id.nav_setting){
            showFragment(new SettingFragment());
            shadowActionBar.setVisibility(View.VISIBLE);
            rippleLayoutForActionButton.setVisibility(View.GONE);
            rippleLayoutForSearchButton.setVisibility(View.GONE);
            floatingActionsMenuFAB.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == btAction){

        }else if (view == ibNav){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        else if (view == ibSearch){
            loadToolBarSearch();
        }
        else if (view== fabMessage){
            FirebaseUser firebaseUser= CommonMethod.getCurrentFirebaseUser();
                if(firebaseUser!=null){
                    GroupChatCreationDialogFragment dialogFragment=
                            new GroupChatCreationDialogFragment(
                                    firebaseUser.getUid(),
                                    firebaseUser.getDisplayName(),
                                    firebaseUser.getPhotoUrl().toString()
                            );
                    dialogFragment.show(getFragmentManager(), "");
                }
        }
        else if (view == fabNew){

            NewStatusDialogFragment newStatusDialogFragment = new NewStatusDialogFragment();
            newStatusDialogFragment.show(getFragmentManager(), "");
        }
    }
    public void loadToolBarSearch() {
//        ArrayList<String> keywordsStored= ReserveSearchKey.loadList(MainActivity.this, CommonMethod.PREFS_NAME, CommonMethod.KEY_COUNTRIES);

        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        CommonMethod.setListViewHeightBasedOnChildren(listSearch);
        edtToolSearch.setHint(getString(R.string.txt_type_name));

        final Dialog toolbarSearchDialog = new Dialog(MainActivity.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(true);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

//        keywordsStored = (keywordsStored != null && keywordsStored.size() > 0) ? keywordsStored : new ArrayList<String>();
//        final SearchAdapter reservedSearchAdapter = new SearchAdapter(MainActivity.this, keywordsStored, false);

        listSearch.setVisibility(View.VISIBLE);

        listSearch.setAdapter(searchFriendAdapter);
        getAllUser();

        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String keyword = String.valueOf(searchFriendAdapter.getItem(position).getName());
//                ReserveSearchKey.addList(MainActivity.this, CommonMethod.PREFS_NAME, CommonMethod.KEY_COUNTRIES, keyword);
                edtToolSearch.setText(keyword);
                listSearch.setVisibility(View.GONE);
                Boolean b= false;
                if (isAFriend.containsKey(searchFriendAdapter.getItem(position).getUid()))
                    b= true;
                UserInfoDialogFragment dialog = new UserInfoDialogFragment(searchFriendAdapter.getItem(position), b);
                dialog.show(getFragmentManager(), "");

            }
        });
        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

//                String[] keyword = MainActivity.this.getResources().getStringArray(R.array.countries_array);
//                allUsers = new ArrayList<String>(Arrays.asList(keyword));
//                listSearch.setVisibility(View.VISIBLE);
//                reservedSearchAdapter.updateList(allUsers, true);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
//                if (s.length() > 0) {
//                    for (int i = 0; i < allUsers.size(); i++) {
//                        if (allUsers.get(i).getName().toLowerCase().startsWith(s.toString().trim().toLowerCase())) {
//
//                            filterList.add(allUsers.get(i));
//
//                            listSearch.setVisibility(View.VISIBLE);
////                            reservedSearchAdapter.updateList(filterList, true);
//                            isNodata = true;
//                        }
                    isNodata =searchFriendAdapter.filter(s.toString());
//                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("No data found");
                    }
                    else {
                        listSearch.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                    }
//                } else {
//                    listSearch.setVisibility(View.GONE);
//                    txtEmpty.setVisibility(View.GONE);
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

        imgToolMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtToolSearch.setText("");

            }
        });


    }
    private void getAllUser(){
        FirebaseDatabase.getInstance().getReference().
                child(Constant.CHILD_USERS).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers.clear();
                //for list friend
                ArrayList<User> allFriends= new ArrayList<>();
                if (friendsOnline!= null)
                    friendsOnline.clear();

                if (friendsOffline!= null)
                    friendsOffline.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    User user= ds.getValue(User.class);
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                        if (user.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            if (isAFriend!= null)
                                isAFriend.clear();
                            if (ds.hasChild(Constant.KEY_FRIENDS)){
                                for (DataSnapshot dss : ds.child(Constant.KEY_FRIENDS).getChildren()){
                                    isAFriend.put(dss.getKey().toString(), true);
                                }
                            }
                        }
                        else{
                            allUsers.add(user);
                            searchFriendAdapter.notifyDataSetChanged();
                        }
                    }
                }
                //for list friend
                for (User user: allUsers) {
                    if (isAFriend.containsKey(user.getUid())) {
                        allFriends.add(user);
                    }
                }
                if (tvTotalFriends!=null)
                    tvTotalFriends.setText(getString(R.string.txt_total)+ allFriends.size()+" "+ getString(R.string.txt_friends));
                for (User user: allFriends){
//                    Log.e("test method", "");
                    if (user.isConnection()){
                        friendsOnline.add(user);

                    }
                    else{
                        friendsOffline.add(user);

                    }
                }
                //for progressbar
                if (allFriends.size() == friendsOnline.size()){
                    progressBarListFriend.setVisibility(View.GONE);
                }
                else
                if (allFriends.size()!= 0){
//                    Log.e(TAG, "progressbar "+(int)(100*(double)friendsOnline.size()/(double)allFriends.size()) );
                    progressBarListFriend.setProgress((int)(100*(double)friendsOnline.size()/(double)allFriends.size()));
                }
                else progressBarListFriend.setProgress(0);
                progressBarListFriend.invalidate();
                friendsOnlineAdapter.notifyDataSetChanged();
                friendsOfflineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setDataToNavHeader(){
        View header = navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView tvName = (TextView) header.findViewById(R.id.tvName_NavHeaderMain);
        TextView tvEmail = (TextView) header.findViewById(R.id.tvEmail_NavHeaderMain);

        CircleImageView civAvatar = (CircleImageView) header.findViewById(R.id.ivAvatar_NavHeaderMain);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            tvEmail.setText(firebaseUser.getEmail());
            tvName.setText(firebaseUser.getDisplayName());
            Glide.with(header.getContext()).load(firebaseUser.getPhotoUrl()).into(civAvatar);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
