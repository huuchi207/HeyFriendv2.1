package dialog;

/**
 * Created by root on 29/10/2016.
 */


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;
import com.chi.heyfriendv21.activity.OneToOneConversationActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import common.CommonMethod;
import common.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import object.User;

/**
 * Created by root on 29/10/2016.
 */

public class UserInfoDialogFragment extends DialogFragment {
    User user;
    Boolean isFriend;
    Button btSendFriendRequest, btRemoveFriend, btSendMessage;
    CircleImageView civAvatar;
    LinearLayout lSendFriendRequest;
    LinearLayout lSendMsgAndRemoveFriend;
    TextView tvDateOfBirth, tvGender, tvUsername;
    DatabaseReference databaseReference;
    FirebaseUser currentFirebaseUser;
    public UserInfoDialogFragment(User friendUser, Boolean isFriend) {
        this.isFriend = isFriend;
        this.user= friendUser;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style of dialog fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UserInfoDialog);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        View view = inflater.inflate(R.layout.dialog_user_info, null);
        //init component
        btRemoveFriend = (Button) view.findViewById(R.id.btRemoveFriend);
        btSendFriendRequest = (Button) view.findViewById(R.id.btSendFriendRequest);
        btSendMessage = (Button) view.findViewById(R.id.btSendMessage);
        lSendFriendRequest = (LinearLayout) view.findViewById(R.id.lSendFriendRequest);
        lSendMsgAndRemoveFriend = (LinearLayout) view.findViewById(R.id.lSendMsgAndRemoveFriend);
        civAvatar = (CircleImageView) view.findViewById(R.id.imvProfileImage);
        tvDateOfBirth= (TextView) view.findViewById(R.id.tvDateOfBirth);
        tvGender = (TextView) view.findViewById(R.id.tvGender);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        //init data ref and firebase user
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentFirebaseUser= CommonMethod.getCurrentFirebaseUser();
        //put data into component
        tvUsername.setText(user.getName());
        tvGender.setText(user.getStringGender(getActivity()));
        tvDateOfBirth.setText(user.getDateOfBirth());
        Glide.with(getActivity()).load(user.getPhotoURL()).into(civAvatar);

        //handle event
        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                String clientUid= user.getUid();
                String myUid= "";
                String myName="";
                String clientName="";
                String myPhotoUrl="";
                String clientPhotoUrl="";
                if (currentFirebaseUser!=null){
                    myUid = currentFirebaseUser.getUid();
                    myName= currentFirebaseUser.getDisplayName();
                    clientName= user.getName();
                    myPhotoUrl= currentFirebaseUser.getPhotoUrl().toString();
                    clientPhotoUrl= user.getPhotoURL();
                }


                Intent resultIntent = new Intent(getActivity(), OneToOneConversationActivity.class);
                resultIntent.putExtra(Constant.CLIENT_UID, clientUid);
                resultIntent.putExtra(Constant.MY_UID, myUid);
                resultIntent.putExtra("My " +Constant.KEY_PHOTOURL,myPhotoUrl);
                resultIntent.putExtra("Client " +Constant.KEY_PHOTOURL,clientPhotoUrl);
                resultIntent.putExtra("client name", clientName);
                resultIntent.putExtra("my name", myName);
                startActivity(resultIntent);
            }
        });
        btSendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                new SendInvitationDialogFragment(user)
                        .show(getActivity().getFragmentManager(), "");
            }
        });

        btRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int choice) {
                    switch (choice) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //in CHILD_FRIENDLISTS of mine
                            if (currentFirebaseUser!=null){
                                databaseReference.child(Constant.CHILD_FRIENDLISTS).child(currentFirebaseUser.getUid()).child(user.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError!= null){
                                            Toast.makeText(getActivity(), R.string.announce_cant_remove_this_friend, Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            //in CHILD_FRIENDLISTS of theirs
                                            databaseReference.getRoot().child(Constant.CHILD_FRIENDLISTS).child(user.getUid()).child(currentFirebaseUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError!= null){
                                                        Toast.makeText(getActivity(), R.string.announce_cant_remove_this_friend, Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        //in CHILD_USER of mine
                                                        databaseReference.getRoot().child(Constant.CHILD_USERS).child(user.getUid()).child(Constant.KEY_FRIENDS).child(currentFirebaseUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                if (databaseError!= null){
                                                                    Toast.makeText(getActivity(), R.string.announce_cant_remove_this_friend, Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    //in CHILD_USER of theirs
                                                                    databaseReference.getRoot().child(Constant.CHILD_USERS).child(currentFirebaseUser.getUid()).child(Constant.KEY_FRIENDS).child(user.getUid()).
                                                                            removeValue(new DatabaseReference.CompletionListener() {
                                                                                @Override
                                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                                    if (databaseError!= null){
                                                                                        Toast.makeText(getActivity(), R.string.announce_cant_remove_this_friend, Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                    else{
                                                                                        Toast.makeText(getActivity(), R.string.announce_friend_is_removed, Toast.LENGTH_SHORT).show();
                                                                                        dismiss();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }


                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you really want to remove this friend?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            }
        });

        if(!isFriend){
            lSendMsgAndRemoveFriend.setVisibility(View.GONE);
            lSendFriendRequest.setVisibility(View.VISIBLE);
        }
        return view;
    }

}


