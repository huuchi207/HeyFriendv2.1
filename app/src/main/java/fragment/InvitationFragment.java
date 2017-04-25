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



public class InvitationFragment extends Fragment implements View.OnClickListener {
    ListView lvUser, lvInvitationMsg;

    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    ArrayList<Invitation> invitations;
    InvitationMesssageAdapter invitationMesssageAdapter;
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
        //set on click
        lvInvitationMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        CommonMethod.showAnimation(v, getActivity());

    }
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

                    }
                    invitationMesssageAdapter.notifyDataSetChanged();
                    if (invitations.size()==0){
                        tvNoInvitation.setVisibility(View.VISIBLE);
                        lvInvitationMsg.setVisibility(View.GONE);
                    }
                    else{
                        tvNoInvitation.setVisibility(View.GONE);
                        lvInvitationMsg.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}
