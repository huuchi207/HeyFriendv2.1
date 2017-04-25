package dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.CommonMethod;
import common.Constant;
import object.Friend;
import object.Invitation;
import object.User;


//COMPLETED
public class SendInvitationDialogFragment extends DialogFragment {
    Button btSend, btCancel;
    TextView tvTitle;
    EditText etInvitationMsg;
    String senderUid="", receiverUid="", receiverName="", senderName="", senderPhotoUrl="";
    DatabaseReference databaseReference;
    FirebaseUser currentFirebaseUser;
    User user;
    public SendInvitationDialogFragment(User user) {
        currentFirebaseUser= CommonMethod.getCurrentFirebaseUser();
        if (currentFirebaseUser!=null){
            this.senderUid = currentFirebaseUser.getUid();
            this.receiverUid = user.getUid();
            this.receiverName = user.getName();
            this.senderName = currentFirebaseUser.getDisplayName();
            this.senderPhotoUrl= currentFirebaseUser.getPhotoUrl().toString();
        }

        this.user= user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style of dialog fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SendInvitationDialog);
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //get view
        View view = inflater.inflate(R.layout.dialog_send_invitation, null);
        //init component
        btCancel = (Button) view.findViewById(R.id.btCancel);
        btSend = (Button) view.findViewById(R.id.btOK);
        etInvitationMsg = (EditText) view.findViewById(R.id.etInvitationMsg);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        //put data into component
        tvTitle.setText(receiverName);


        //handle event
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                String content= etInvitationMsg.getText().toString().trim();
                if (!content.equals("")){
                    if (currentFirebaseUser!=null){
                        Friend sender = new Friend(currentFirebaseUser.getUid(),
                                currentFirebaseUser.getPhotoUrl().toString(),
                                currentFirebaseUser.getDisplayName(), null, false );

                        databaseReference.child(Constant.CHILD_INVITATIONS).
                                child(receiverUid).child(senderUid).setValue(new Invitation(content, CommonMethod.getCurrentTime(), sender), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError!= null){
                                    Toast.makeText(getActivity(), R.string.announce_cant_send_invitation, Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), R.string.announce_invitation_msg_is_sent, Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });
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

}
