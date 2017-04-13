package com.chi.heyfriendv21.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.chi.heyfriendv21.R;
import com.firebase.client.Firebase;
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

import adapter.SearchAdapter;
import adapter.SearchFriendAdapter;
import common.CommonMethod;
import common.Constant;
import common.ReserveSearchKey;
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
    ArrayList<User> allUsers= new ArrayList<>();
    Map<String, Boolean> isAFriend= new HashMap<>();
    SearchFriendAdapter searchFriendAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        FirebaseAuth.AuthStateListener mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
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
        //assign onClickListener
        btAction.setOnClickListener(this);
        ibNav.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
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
        if (id == R.id.nav_timeline) {
            showFragment(new TimelineFragment());
            shadowActionBar.setVisibility(View.VISIBLE);
            rippleLayoutForActionButton.setVisibility(View.VISIBLE);
            rippleLayoutForSearchButton.setVisibility(View.GONE);
        }
        else if (id == R.id.nav_conversation){
            showFragment(new MessageFragment());
            shadowActionBar.setVisibility(View.GONE);
            rippleLayoutForActionButton.setVisibility(View.VISIBLE);
            rippleLayoutForSearchButton.setVisibility(View.GONE);
        }
        else if (id== R.id.nav_invitation){
            showFragment(new InvitationFragment());
            shadowActionBar.setVisibility(View.VISIBLE);
            rippleLayoutForActionButton.setVisibility(View.GONE);
            rippleLayoutForSearchButton.setVisibility(View.VISIBLE);
        }
        else if (id==R.id.nav_setting){
            showFragment(new SettingFragment());
            shadowActionBar.setVisibility(View.VISIBLE);
            rippleLayoutForActionButton.setVisibility(View.GONE);
            rippleLayoutForSearchButton.setVisibility(View.GONE);
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
        edtToolSearch.setHint("Type name");

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
        searchFriendAdapter = new SearchFriendAdapter(this, R.layout.item_search_friend,allUsers);
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
    void getAllUser(){
        FirebaseDatabase.getInstance().getReference().
                child(Constant.CHILD_USERS).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers.clear();
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
