package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.InvitationMesssageAdapter;
import adapter.SearchFriendAdapter;
import common.CommonMethod;
import common.Constant;
import dialog.UserInfoDialogFragment;
import object.Invitation;
import object.User;


/**
 * Created by huuchi207 on 17/10/2016.
 */

public class InvitationFragment extends Fragment implements View.OnClickListener {
    ListView lvUser, lvInvitationMsg;
    Button btSearch;
    EditText etSearch;
    LinearLayout llSearchBar;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    ArrayList<Invitation> invitations;
    ArrayList<User> allUsers;
    Map<String, Boolean> isAFriend;
    InvitationMesssageAdapter invitationMesssageAdapter;
    SearchFriendAdapter searchFriendAdapter;
    TextView tvNoInvitation;
    public InvitationFragment (){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);
        //init component
        btSearch= (Button) view.findViewById(R.id.btSearch);
        etSearch =(EditText) view.findViewById(R.id.etSearch);
        llSearchBar =(LinearLayout) view.findViewById(R.id.llSearchBar);
        lvInvitationMsg =(ListView) view.findViewById(R.id.lvInvitation);
        lvUser= (ListView) view.findViewById(R.id.lvUser);
        tvNoInvitation = (TextView) view.findViewById(R.id.tvNoInvitation);
        //init firebase
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser= firebaseAuth.getCurrentUser();

        //init arraylist and adapter
        invitations = new ArrayList<>();
        invitationMesssageAdapter= new InvitationMesssageAdapter(getActivity(), R.layout.item_invitation_msg, invitations);
        getInvitations();
        lvInvitationMsg.setAdapter(invitationMesssageAdapter);

        allUsers = new ArrayList<>();

//        searchFriendAdapter = new SearchFriendAdapter(getActivity(), R.layout.item_search_friend, allUsers);
//        getAllUser();
//        lvUser.setAdapter(searchFriendAdapter);
        isAFriend = new HashMap<>();
        //set on click
//        btSearch.setOnClickListener(this);
//        etSearch.setOnClickListener(this);
        //on item click listener
//        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Boolean b= false;
//                if (isAFriend.containsKey(searchFriendAdapter.getItem(position).getUid()))
//                    b= true;
//                UserInfoDialogFragment dialog = new UserInfoDialogFragment(searchFriendAdapter.getItem(position), b);
//                dialog.show(getActivity().getFragmentManager(), "");
//            }
//        });
        lvInvitationMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //listen on edit text
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Call back the Adapter with current character to Filter
////                searchFriendAdapter.getFilter().filter(s.toString());
////                if (etSearch.getText().toString().trim().equals("")){
////                    lvInvitationMsg.setVisibility(View.VISIBLE);
////                    lvUser.setVisibility(View.GONE);
////                }
////                else{
////                    lvInvitationMsg.setVisibility(View.GONE);
////                    lvUser.setVisibility(View.VISIBLE);
////                }
//                searchFriendAdapter.filter(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
        return view;
    }

    @Override
    public void onClick(View v) {
        CommonMethod.showAnimation(v, getActivity());
//        if(v == btSearch){
//
//
//            if (etSearch.getVisibility()== View.VISIBLE){
//                lvInvitationMsg.setVisibility(View.VISIBLE);
//                etSearch.setVisibility(View.INVISIBLE);
//                lvUser.setVisibility(View.GONE);
//                if (invitations.size()==0){
//                    tvNoInvitation.setVisibility(View.VISIBLE);
//                }
//                else{
//                    tvNoInvitation.setVisibility(View.GONE);
//                }
//            }
//            else{
//                etSearch.setVisibility(View.VISIBLE);
//                tvNoInvitation.setVisibility(View.GONE);
////                if (etSearch.getText().toString().trim().equals("")){
////                    lvInvitationMsg.setVisibility(View.VISIBLE);
////                    lvUser.setVisibility(View.GONE);
////                }else {
//                    lvInvitationMsg.setVisibility(View.GONE);
//                    lvUser.setVisibility(View.VISIBLE);
//                searchFriendAdapter.filter(etSearch.getText().toString());
////                }
//            }
//
//            if (!CommonMethod.isNetworkConnected(getActivity())){
//
//            }
//        }

    }
//    void getAllUser(){
//        databaseReference.child(Constant.CHILD_USERS).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                User user= dataSnapshot.getValue(User.class);
//                if (user.getUid().equals(currentUser.getUid())){
//                    if (dataSnapshot.hasChild(Constant.KEY_FRIENDS)){
//                        for (DataSnapshot ds : dataSnapshot.child(Constant.KEY_FRIENDS).getChildren()){
//                            isAFriend.put(ds.getKey().toString(), true);
//                        }
//                    }
//                }
//                else{
//                    allUsers.add(user);
//                    searchFriendAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                User user= dataSnapshot.getValue(User.class);
//                if (user.getUid().equals(currentUser.getUid())){
//                    isAFriend.clear();
//                    if (dataSnapshot.hasChild(Constant.KEY_FRIENDS)){
//                        for (DataSnapshot ds : dataSnapshot.child(Constant.KEY_FRIENDS).getChildren()){
//                            isAFriend.put(ds.getKey().toString(), true);
//                        }
//                    }
//                }
//                else {
//
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
    void getAllUser(){
        databaseReference.child(Constant.CHILD_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    User user= ds.getValue(User.class);
                    if (currentUser!=null){
                        if (user.getUid().equals(currentUser.getUid())){
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
//    void getInvitations(){
//        databaseReference.child(Constant.CHILD_INVITATIONS).child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Invitation invitation= dataSnapshot.getValue(Invitation.class);
//                invitations.add(invitation);
//                invitationMesssageAdapter.notifyDataSetChanged();
////                Log.e("user------", user.getName());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
////                Invitation invitation= dataSnapshot.getValue(Invitation.class);
////                invitations.remove(invitation);
////                invitationMesssageAdapter.notifyDataSetChanged();
////                Log.e("on child removed", "");
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
    void getInvitations(){
        if (currentUser!=null){
            databaseReference.child(Constant.CHILD_INVITATIONS).child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (invitations!= null)
                        invitations.clear();

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Invitation invitation= ds.getValue(Invitation.class);
                        invitations.add(invitation);
                        invitationMesssageAdapter.notifyDataSetChanged();
                    }
                    if (invitations.size()==0){
                        tvNoInvitation.setVisibility(View.VISIBLE);
                    }
                    else{
                        tvNoInvitation.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}
